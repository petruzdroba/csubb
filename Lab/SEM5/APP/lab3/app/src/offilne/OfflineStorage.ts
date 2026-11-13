import { ItemProps } from '../todo/ItemProps';

export type PendingOperation = 'create' | 'update';

export interface PendingItem {
  localId: string;
  operation: PendingOperation;
  item: ItemProps;
}

const getStorageKey = (token: string) => {
  try {
    const payload = JSON.parse(atob(
      token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    ));

    return `pendingItems_${payload._id}`;
  } catch {
    throw new Error('Invalid authentication token');
  }
};

export const getPendingItems = (token: string): PendingItem[] => {
  const data = localStorage.getItem(getStorageKey(token));

  if (!data) return [];

  return JSON.parse(data).map((entry: PendingItem) => ({
    ...entry,
    item: {
      ...entry.item,
      date_opened: new Date(entry.item.date_opened)
    }
  }));
};

export const savePendingItems = (token: string, items: PendingItem[]) => {
  localStorage.setItem(getStorageKey(token), JSON.stringify(items));
};

const getCachedItemsKey = (token: string, search: string, currency: string) =>
  `${getStorageKey(token).replace('pendingItems_', 'cachedItems_')}_${encodeURIComponent(search)}_${encodeURIComponent(currency)}`;

export const getCachedItems = (
  token: string,
  search: string,
  currency: string
): ItemProps[] => {
  const data = localStorage.getItem(getCachedItemsKey(token, search, currency));

  if (!data) return [];

  return JSON.parse(data).map((item: ItemProps) => ({
    ...item,
    date_opened: new Date(item.date_opened)
  }));
};

export const saveCachedItems = (
  token: string,
  search: string,
  currency: string,
  items: ItemProps[],
  append = false
) => {
  const key = getCachedItemsKey(token, search, currency);
  const cachedItems = append ? getCachedItems(token, search, currency) : [];
  const updatedItems = [...cachedItems];

  items.forEach(item => {
    const index = updatedItems.findIndex(cached => cached._id === item._id);

    if (index === -1) {
      updatedItems.push(item);
    } else {
      updatedItems[index] = item;
    }
  });

  localStorage.setItem(key, JSON.stringify(updatedItems));
};