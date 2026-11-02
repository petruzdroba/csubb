import axios from 'axios';
import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import { ItemProps } from './ItemProps';

const itemUrl = `http://${baseUrl}/api/item`;

export const getItems = (
  token: string,
  skip: number,
  limit: number = 5,
  search: string = '',
  currency: string = ''
): Promise<ItemProps[]> => {
  const params = new URLSearchParams({
    skip: String(skip),
    limit: String(limit),
    search,
    currency
  });

  return withLogs(
    axios.get(`${itemUrl}?${params.toString()}`, authConfig(token)),
    'getItems'
  );
};

export const createItem = (token: string, item: ItemProps): Promise<ItemProps> => {
  return withLogs(axios.post(itemUrl, item, authConfig(token)), 'createItem');
};

export const updateItem = (token: string, item: ItemProps): Promise<ItemProps> => {
  return withLogs(axios.put(`${itemUrl}/${item._id}`, item, authConfig(token)), 'updateItem');
};

interface MessageData {
  type: 'created' | 'updated';
  payload: ItemProps;
}

const log = getLogger('ws');

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
  const ws = new WebSocket(`ws://${baseUrl}`);
  ws.onopen = () => {
    log('web socket onopen');
    ws.send(JSON.stringify({ type: 'authorization', payload: { token } }));
  };
  ws.onclose = () => {
    log('web socket onclose');
  };
  ws.onerror = error => {
    log('web socket onerror', error);
  };
  ws.onmessage = messageEvent => {
    log('web socket onmessage');
    onMessage(JSON.parse(messageEvent.data));
  };
  return () => {
    ws.close();
  }
}
