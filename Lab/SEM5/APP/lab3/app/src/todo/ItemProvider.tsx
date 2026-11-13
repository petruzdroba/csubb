import React, { useCallback, useContext, useEffect, useReducer, useRef, useState } from 'react';
import { getLogger } from '../core';
import { useNetwork } from '../core/NetworkStatus';
import { ItemProps } from './ItemProps';
import { createItem, getItems, newWebSocket, updateItem } from './itemApi';
import { AuthContext } from '../auth';
import {
  getCachedItems,
  getPendingItems,
  saveCachedItems,
  savePendingItems,
  PendingItem
} from '../offilne/OfflineStorage';

const log = getLogger('ItemProvider');

type SaveItemFn = (item: ItemProps) => Promise<void>;

export interface ItemsState {
  items: ItemProps[];
  pendingItems: PendingItem[];
  unsentCount: number;
  fetching: boolean;
  fetchingError?: Error | null;
  saving: boolean;
  savingError?: Error | null;
  saveItem?: SaveItemFn;
  hasMore: boolean;
  loadingMore: boolean;
  loadMoreItems?: () => Promise<void>;
  search: string;
  currency: string;
  setSearch?: (value: string) => void;
  setCurrency?: (value: string) => void;
}

interface ActionProps {
  type: string;
  payload?: any;
}

const initialState: ItemsState = {
  items: [],
  pendingItems: [],
  unsentCount: 0,
  fetching: false,
  saving: false,
  hasMore: true,
  loadingMore: false,
  search: '',
  currency: ''
};

const FETCH_ITEMS_STARTED = 'FETCH_ITEMS_STARTED';
const FETCH_ITEMS_SUCCEEDED = 'FETCH_ITEMS_SUCCEEDED';
const FETCH_ITEMS_FAILED = 'FETCH_ITEMS_FAILED';
const LOAD_PENDING_ITEMS = 'LOAD_PENDING_ITEMS';
const SAVE_ITEM_STARTED = 'SAVE_ITEM_STARTED';
const SAVE_ITEM_SUCCEEDED = 'SAVE_ITEM_SUCCEEDED';
const SAVE_ITEM_FAILED = 'SAVE_ITEM_FAILED';
const SYNC_ITEM_SUCCEEDED = 'SYNC_ITEM_SUCCEEDED';
const RESET_ITEMS = 'RESET_ITEMS';
const LOAD_MORE_STARTED = 'LOAD_MORE_STARTED';
const LOAD_MORE_SUCCEEDED = 'LOAD_MORE_SUCCEEDED';
const LOAD_MORE_FAILED = 'LOAD_MORE_FAILED';

const upsertItem = (items: ItemProps[], item: ItemProps) => {
  const index = items.findIndex(it => it._id === item._id);

  if (index === -1) {
    return [item, ...items];
  }

  const updated = [...items];
  updated[index] = item;
  return updated;
};

const reducer = (state: ItemsState, { type, payload }: ActionProps): ItemsState => {
  switch (type) {
    case FETCH_ITEMS_STARTED:
      return {
        ...state,
        items: [],
        fetching: true,
        fetchingError: null,
        hasMore: true
      };

    case FETCH_ITEMS_SUCCEEDED: {
      let items = [...payload.items];

      payload.visiblePendingItems.forEach((entry: PendingItem) => {
        items = upsertItem(items, entry.item);
      });

      return {
        ...state,
        items,
        pendingItems: payload.pendingItems,
        unsentCount: payload.pendingItems.length,
        fetching: false,
        hasMore: payload.items.length === 5
      };
    }

    case FETCH_ITEMS_FAILED:
      return {
        ...state,
        fetchingError: payload.error,
        fetching: false
      };

    case LOAD_PENDING_ITEMS:
      return {
        ...state,
        pendingItems: payload.pendingItems,
        unsentCount: payload.pendingItems.length
      };

    case SAVE_ITEM_STARTED:
      return { ...state, savingError: null, saving: true };

    case SAVE_ITEM_SUCCEEDED:
      return {
        ...state,
        items: upsertItem(state.items, payload.item),
        pendingItems: payload.pendingItems,
        unsentCount: payload.pendingItems.length,
        saving: false
      };

    case SAVE_ITEM_FAILED:
      return {
        ...state,
        savingError: payload.error,
        saving: false
      };

    case SYNC_ITEM_SUCCEEDED: {
      const items = state.items.filter(it => it._id !== payload.localId);

      return {
        ...state,
        items: upsertItem(items, payload.item),
        pendingItems: payload.pendingItems,
        unsentCount: payload.pendingItems.length
      };
    }

    case RESET_ITEMS:
      return { ...initialState };

    case LOAD_MORE_STARTED:
      return { ...state, loadingMore: true };

    case LOAD_MORE_SUCCEEDED: {
      const items = [...state.items];

      payload.items.forEach((item: ItemProps) => {
        if (!items.some(existing => existing._id === item._id)) {
          items.push(item);
        }
      });

      return {
        ...state,
        items,
        hasMore: payload.items.length === 5,
        loadingMore: false
      };
    }

    case LOAD_MORE_FAILED:
      return { ...state, loadingMore: false };

    default:
      return state;
  }
};

export const ItemContext = React.createContext<ItemsState>(initialState);

interface ItemProviderProps {
  children: React.ReactNode;
}

export const ItemProvider: React.FC<ItemProviderProps> = ({ children }) => {
  const { token } = useContext(AuthContext);
  const { isOnline } = useNetwork();
  const [state, dispatch] = useReducer(reducer, initialState);

  const [search, setSearch] = useState('');
  const [currency, setCurrency] = useState('');

  const syncing = useRef(false);
  const pageOffset = useRef(0);
  const loadingMoreRef = useRef(false);

  const {
    items,
    pendingItems,
    unsentCount,
    fetching,
    fetchingError,
    saving,
    savingError,
    hasMore,
    loadingMore
  } = state;

  useEffect(() => {
    pageOffset.current = 0;
    loadingMoreRef.current = false;

    dispatch({ type: RESET_ITEMS });

    if (token) {
      const pendingItems = getPendingItems(token);
      dispatch({ type: LOAD_PENDING_ITEMS, payload: { pendingItems } });
    }
  }, [token]);

  useEffect(getItemsEffect, [token, search, currency, isOnline]);
  useEffect(wsEffect, [token]);

  const syncPending = useCallback(async () => {
    if (!token || !isOnline || syncing.current) {
      return;
    }

    syncing.current = true;

    try {
      const pendingItems = getPendingItems(token);

      for (const entry of pendingItems) {
        try {
          let savedItem: ItemProps;

          if (entry.operation === 'create') {
            const { _id, ...item } = entry.item;
            savedItem = await createItem(token, item as ItemProps);
          } else {
            savedItem = await updateItem(token, entry.item);
          }

          const currentPending = getPendingItems(token);

          const remaining = currentPending.filter(
            p => p.localId !== entry.localId
          );

          savePendingItems(token, remaining);

          dispatch({
            type: SYNC_ITEM_SUCCEEDED,
            payload: {
              localId: entry.localId,
              item: savedItem,
              pendingItems: remaining
            }
          });

          log('synchronized item', entry.localId);
        } catch (error) {
          log('synchronization failed', entry.localId, error);
          break;
        }
      }
    } finally {
      syncing.current = false;
    }
  }, [token, isOnline]);

  useEffect(() => {
    if (isOnline) {
      syncPending();
    }
  }, [isOnline, syncPending]);

  const saveItem = useCallback<SaveItemFn>(async item => {
    if (!token) return;

    dispatch({ type: SAVE_ITEM_STARTED });

    try {
      let pending = getPendingItems(token);

      const existingPending = pending.find(
        entry => entry.item._id === item._id
      );

      if (existingPending) {
        existingPending.item = item;

        pending = pending.map(entry =>
          entry.localId === existingPending.localId ? existingPending : entry
        );

        savePendingItems(token, pending);

        dispatch({
          type: SAVE_ITEM_SUCCEEDED,
          payload: { item, pendingItems: pending }
        });

        if (isOnline) {
          await syncPending();
        }

        return;
      }

      if (isOnline) {
        try {
          const savedItem = item._id
            ? await updateItem(token, item)
            : await createItem(token, item);

          dispatch({
            type: SAVE_ITEM_SUCCEEDED,
            payload: { item: savedItem, pendingItems: pending }
          });

          return;
        } catch (error) {
          log('REST request failed, saving locally', error);
        }
      }

      const localId = item._id || `local-${Date.now()}-${Math.random()}`;

      const localItem = {
        ...item,
        _id: localId
      };

      pending.push({
        localId,
        operation: item._id ? 'update' : 'create',
        item: localItem
      });

      savePendingItems(token, pending);

      dispatch({
        type: SAVE_ITEM_SUCCEEDED,
        payload: { item: localItem, pendingItems: pending }
      });
    } catch (error) {
      log('saveItem failed', error);
      dispatch({ type: SAVE_ITEM_FAILED, payload: { error } });
    }
  }, [token, isOnline, syncPending]);

  const loadMoreItems = useCallback(async () => {
    if (
      !token ||
      !isOnline ||
      !hasMore ||
      fetching ||
      loadingMoreRef.current
    ) {
      return;
    }

    loadingMoreRef.current = true;
    dispatch({ type: LOAD_MORE_STARTED });

    try {
      const newItems = await getItems(
        token,
        pageOffset.current,
        5,
        search,
        currency
      );

      saveCachedItems(token, search, currency, newItems, true);
      pageOffset.current += newItems.length;

      dispatch({
        type: LOAD_MORE_SUCCEEDED,
        payload: { items: newItems }
      });
    } catch (error) {
      log('loadMoreItems failed', error);
      dispatch({ type: LOAD_MORE_FAILED });
    } finally {
      loadingMoreRef.current = false;
    }
  }, [token, isOnline, hasMore, fetching, search, currency]);

  const value: ItemsState = {
    items,
    pendingItems,
    unsentCount,
    fetching,
    fetchingError,
    saving,
    savingError,
    saveItem,
    hasMore,
    loadingMore,
    loadMoreItems,
    search,
    currency,
    setSearch,
    setCurrency
  };

  log('returns');

  return (
    <ItemContext.Provider value={value}>
      {children}
    </ItemContext.Provider>
  );

  function getItemsEffect() {
    let canceled = false;

    pageOffset.current = 0;
    loadingMoreRef.current = false;

    if (token) {
      fetchItems();
    }

    return () => {
      canceled = true;
    };

    async function fetchItems() {
      try {
        dispatch({ type: FETCH_ITEMS_STARTED });

        const pendingItems = getPendingItems(token!);

        const visiblePendingItems = pendingItems.filter(entry => {
          const matchesSearch = entry.item.name
            .toLowerCase()
            .includes(search.toLowerCase());

          const matchesCurrency = !currency ||
            entry.item.currency.toUpperCase() === currency;

          return matchesSearch && matchesCurrency;
        });

        const serverItems = isOnline
          ? await getItems(token!, 0, 5, search, currency)
          : getCachedItems(token!, search, currency);

        if (canceled) return;

        if (isOnline) {
          saveCachedItems(token!, search, currency, serverItems);
        }
        pageOffset.current = serverItems.length;

        dispatch({
          type: FETCH_ITEMS_SUCCEEDED,
          payload: {
            items: serverItems,
            pendingItems,
            visiblePendingItems
          }
        });
      } catch (error) {
        if (!canceled) {
          dispatch({ type: FETCH_ITEMS_FAILED, payload: { error } });
        }
      }
    }
  }

  function wsEffect() {
    let canceled = false;
    let closeWebSocket: (() => void) | undefined;

    if (token?.trim()) {
      closeWebSocket = newWebSocket(token, message => {
        if (canceled) return;

        const { type, payload: item } = message;

        if (type === 'created' || type === 'updated') {
          dispatch({
            type: SAVE_ITEM_SUCCEEDED,
            payload: { item, pendingItems: getPendingItems(token) }
          });
        }
      });
    }

    return () => {
      canceled = true;
      closeWebSocket?.();
    };
  }
};