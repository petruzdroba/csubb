import React, { useContext, useEffect, useRef } from 'react';
import { RouteComponentProps } from 'react-router';
import {
  IonContent,
  IonFab,
  IonFabButton,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonList,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
  IonButton,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonSearchbar,
  IonSelect,
  IonSelectOption
} from '@ionic/react';
import { add } from 'ionicons/icons';
import Item from './Item';
import { getLogger } from '../core';
import { ItemContext } from './ItemProvider';
import { AuthContext } from '../auth/AuthProvider';

const log = getLogger('ItemList');

const ItemList: React.FC<RouteComponentProps> = ({ history }) => {
  const {
    items,
    fetching,
    fetchingError,
    pendingItems,
    unsentCount,
    hasMore,
    loadingMore,
    loadMoreItems,
    search,
    currency,
    setSearch,
    setCurrency
  } = useContext(ItemContext);

  const { logout } = useContext(AuthContext);

  const contentRef = useRef<HTMLIonContentElement>(null);
  const checkingRef = useRef(false);

  const pendingIds = new Set(pendingItems.map(entry => entry.localId));

  useEffect(() => {
    const checkContent = async () => {
      if (
        checkingRef.current ||
        fetching ||
        loadingMore ||
        !hasMore
      ) {
        return;
      }

      checkingRef.current = true;

      try {
        const scrollElement = await contentRef.current?.getScrollElement();

        if (
          scrollElement &&
          scrollElement.scrollHeight <= scrollElement.clientHeight
        ) {
          await loadMoreItems?.();
        }
      } finally {
        checkingRef.current = false;
      }
    };

    checkContent();

    window.addEventListener('resize', checkContent);

    return () => {
      window.removeEventListener('resize', checkContent);
    };
  }, [items.length, fetching, loadingMore, hasMore, loadMoreItems]);

  log('render', fetching);

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonButton
            slot="end"
            fill="clear"
            onClick={async () => {
              await logout?.();
              history.replace('/login');
            }}
          >
            LogOut
          </IonButton>
          <IonTitle>Bank accounts</IonTitle>
        </IonToolbar>
      </IonHeader>

      <IonContent ref={contentRef}>
        <IonLoading isOpen={fetching} message="Fetching items" />

        <IonSearchbar
          value={search}
          debounce={300}
          placeholder="Search accounts by name"
          onIonInput={event => setSearch?.(event.detail.value ?? '')}
        />

        <IonItem>
          <IonLabel>Filter by currency</IonLabel>
          <IonSelect
            value={currency}
            placeholder="All currencies"
            onIonChange={event => setCurrency?.(event.detail.value ?? '')}
          >
            <IonSelectOption value="">All currencies</IonSelectOption>
            <IonSelectOption value="RON">RON</IonSelectOption>
            <IonSelectOption value="EUR">EUR</IonSelectOption>
            <IonSelectOption value="USD">USD</IonSelectOption>
            <IonSelectOption value="GBP">GBP</IonSelectOption>
            <IonSelectOption value="CAD">CAD</IonSelectOption>
            <IonSelectOption value="AUD">AUD</IonSelectOption>
          </IonSelect>
        </IonItem>

        {unsentCount > 0 && (
          <IonItem lines="none">
            <IonLabel>
              {unsentCount} {unsentCount === 1 ? 'item' : 'items'} not synchronized
            </IonLabel>
          </IonItem>
        )}

        <IonItem>
          <IonLabel>Name</IonLabel>
          <IonLabel>Date opened</IonLabel>
          <IonLabel>Amount</IonLabel>
          <IonLabel>Currency</IonLabel>
        </IonItem>

        <IonList>
          {items.map(({ _id, name, currency, date_opened, ammount }) => (
            <React.Fragment key={_id}>
              <Item
                _id={_id}
                name={name}
                currency={currency}
                date_opened={date_opened}
                ammount={ammount}
                onEdit={id => history.push(`/item/${id}`)}
              />

              {pendingIds.has(_id || '') && (
                <IonItem lines="none">
                  <IonLabel color="warning">
                    Not synchronized — stored locally
                  </IonLabel>
                </IonItem>
              )}
            </React.Fragment>
          ))}
        </IonList>

        {!fetching && items.length === 0 && (
          <IonItem lines="none">
            <IonLabel className="ion-text-center">
              No accounts found
            </IonLabel>
          </IonItem>
        )}

        <IonInfiniteScroll
          threshold="100px"
          disabled={!hasMore}
          onIonInfinite={async event => {
            await loadMoreItems?.();
            await event.target.complete();
          }}
        >
          <IonInfiniteScrollContent loadingText="Loading more items..." />
        </IonInfiniteScroll>

        {fetchingError && (
          <div>{fetchingError.message || 'Failed to fetch items'}</div>
        )}

        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push('/item')}>
            <IonIcon icon={add} />
          </IonFabButton>
        </IonFab>
      </IonContent>
    </IonPage>
  );
};

export default ItemList;