import Router from 'koa-router';
import jwt from 'jsonwebtoken';
import dataStore from "nedb-promise";
import { jwtConfig, revokedTokens } from './utils.js';

export class UserStore {
  constructor({ filename, autoload }) {
    this.store = dataStore({ filename, autoload });
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(user) {
    return this.store.insert(user);
  };
}

const userStore = new UserStore({ filename: './db/users.json', autoload: true });

const createToken = (user) => {
  return jwt.sign({ username: user.username, _id: user._id }, jwtConfig.secret, { expiresIn: 60 * 60 * 60 });
};

export const authRouter = new Router();

authRouter.post('/signup', async (ctx) => {
  try {
    const user = ctx.request.body;
    await userStore.insert(user);
    ctx.response.body = { token: createToken(user) };
    ctx.response.status = 201; // created
  } catch (err) {
    ctx.response.body = { error: err.message };
    ctx.response.status = 400; // bad request
  }

  await createUser(ctx.request.body, ctx.response)
});

authRouter.post('/login', async (ctx) => {
  const credentials = ctx.request.body;
  const user = await userStore.findOne({ username: credentials.username });
  if (user && credentials.password === user.password) {
    ctx.response.body = { token: createToken(user) };
    ctx.response.status = 201; // created
  } else {
    ctx.response.body = { error: 'Invalid credentials' };
    ctx.response.status = 400; // bad request
  }
});

authRouter.post('/logout', async (ctx) => {
  const authorization = ctx.headers.authorization;
  const token = authorization?.startsWith('Bearer ')
    ? authorization.slice(7)
    : null;

  if (!token) {
    ctx.status = 401;
    ctx.body = { error: 'Missing token' };
    return;
  }

  try {
    jwt.verify(token, jwtConfig.secret);
    revokedTokens.add(token);
    ctx.status = 200;
    ctx.body = { message: 'Logged out successfully' };
  } catch {
    ctx.status = 401;
    ctx.body = { error: 'Invalid token' };
  }
});