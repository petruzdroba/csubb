import multer from '@koa/multer';
import fs from 'fs';
import path from 'path';
import { randomUUID } from 'crypto';
import Router from 'koa-router';
import dataStore from 'nedb-promise';
import { broadcast } from './wss.js';

export class ItemStore {
  constructor({ filename, autoload }) {
    this.store = dataStore({ filename, autoload });
  }

  async find(props) {
    return this.store.find(props);
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(item) {
    if (!item.name) throw new Error('Missing name property');
    if (!item.currency) throw new Error('Missing currency property');
    if (item.ammount == null) throw new Error('Missing ammount property');
    if (!item.date_opened) throw new Error('Missing date_opened property');

    return this.store.insert(item);
  }

  async update(props, item) {
    return this.store.update(props, item);
  }

  async remove(props) {
    return this.store.remove(props);
  }
}

const itemStore = new ItemStore({
  filename: './db/items.json',
  autoload: true
});

const uploadDir = 'public/uploads';
fs.mkdirSync(uploadDir, { recursive: true });

const upload = multer({
  storage: multer.diskStorage({
    destination: (req, file, cb) => cb(null, uploadDir),
    filename: (req, file, cb) => {
      cb(null, `${randomUUID()}${path.extname(file.originalname).toLowerCase()}`);
    }
  }),
  limits: { fileSize: 5 * 1024 * 1024 },
  fileFilter: (req, file, cb) => {
    cb(null, ['image/jpeg', 'image/png', 'image/webp'].includes(file.mimetype));
  }
});

export const itemRouter = new Router();

itemRouter.get('/', async (ctx) => {
  const userId = ctx.state.user._id;
  const skip = Math.max(0, parseInt(ctx.query.skip) || 0);
  const limit = Math.min(100, Math.max(1, parseInt(ctx.query.limit) || 5));

  const search = (ctx.query.search || '').trim().toLowerCase();
  const currency = (ctx.query.currency || '').trim().toUpperCase();

  let items = await itemStore.find({ userId });

  if (search) {
    items = items.filter(item =>
      item.name.toLowerCase().includes(search)
    );
  }

  if (currency) {
    items = items.filter(item =>
      item.currency.toUpperCase() === currency
    );
  }

  ctx.body = items.slice(skip, skip + limit);
  ctx.status = 200;
});

itemRouter.get('/:id', async (ctx) => {
  const userId = ctx.state.user._id;

  const item = await itemStore.findOne({
    _id: ctx.params.id,
    userId
  });

  if (!item) {
    ctx.status = 404;
    return;
  }

  ctx.body = item;
  ctx.status = 200;
});

const createItem = async (ctx, item) => {
  try {
    const userId = ctx.state.user._id;

    const createdItem = await itemStore.insert({
      ...item,
      userId
    });

    ctx.body = createdItem;
    ctx.status = 201;

    broadcast(userId, {
      type: 'created',
      payload: createdItem
    });
  } catch (err) {
    ctx.body = { message: err.message };
    ctx.status = 400;
  }
};

itemRouter.post('/', async (ctx) => {
  await createItem(ctx, ctx.request.body);
});

itemRouter.put('/:id', async (ctx) => {
  const userId = ctx.state.user._id;
  const id = ctx.params.id;
  const item = ctx.request.body;

  if (item._id && item._id !== id) {
    ctx.body = { message: 'Param id and body _id should be the same' };
    ctx.status = 400;
    return;
  }

  const updatedItem = {
    ...item,
    _id: id,
    userId
  };

  const updatedCount = await itemStore.update(
    { _id: id, userId },
    { $set: updatedItem }
  );

  if (updatedCount !== 1) {
    ctx.body = { message: 'Resource not found' };
    ctx.status = 404;
    return;
  }

  ctx.body = updatedItem;
  ctx.status = 200;

  broadcast(userId, {
    type: 'updated',
    payload: updatedItem
  });
});

itemRouter.del('/:id', async (ctx) => {
  const userId = ctx.state.user._id;

  const deletedCount = await itemStore.remove({
    _id: ctx.params.id,
    userId
  });

  if (deletedCount === 0) {
    ctx.status = 404;
    return;
  }

  ctx.status = 204;
});

itemRouter.post('/:id/avatar', upload.single('avatar'), async ctx => {
  const userId = ctx.state.user._id;
  const id = ctx.params.id;

  const item = await itemStore.findOne({ _id: id, userId });

  if (!item) {
    if (ctx.file) fs.unlinkSync(ctx.file.path);
    ctx.status = 404;
    ctx.body = { message: 'Resource not found' };
    return;
  }

  if (!ctx.file) {
    ctx.status = 400;
    ctx.body = { message: 'No image uploaded' };
    return;
  }

  const avatarUrl = `/uploads/${ctx.file.filename}`;

  await itemStore.update(
    { _id: id, userId },
    { $set: { avatarUrl } }
  );

  ctx.status = 200;
  ctx.body = { avatarUrl };
});