import React, { useContext, useEffect, useRef } from "react";
import { RouteComponentProps } from "react-router";
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
  IonSelectOption,
  IonModal,
  IonButtons,
  createAnimation,
} from "@ionic/react";
import { add } from "ionicons/icons";
import Item from "./Item";
import { getLogger } from "../core";
import { ItemContext } from "./ItemProvider";
import { AuthContext } from "../auth/AuthProvider";
import { getItems } from "./itemApi";
import { ItemProps } from "./ItemProps";
import AccountsMap from "./AccountsMap";
import { useNetwork } from "../core/NetworkStatus";

const log = getLogger("ItemList");

const mapEnterAnimation = (baseEl: HTMLElement) => {
  const wrapper = baseEl.shadowRoot?.querySelector('.modal-wrapper');

  if (!wrapper) return createAnimation();

  return createAnimation()
    .addElement(wrapper)
    .duration(850)
    .easing('cubic-bezier(0.16, 1, 0.3, 1)')
    .fromTo('opacity', '0', '1')
    .fromTo(
      'transform',
      'translateY(100vh) scale(0.65) rotate(-4deg)',
      'translateY(0) scale(1) rotate(0deg)'
    );
};

const mapLeaveAnimation = (baseEl: HTMLElement) => {
  const wrapper = baseEl.shadowRoot?.querySelector('.modal-wrapper');

  if (!wrapper) return createAnimation();

  return createAnimation()
    .addElement(wrapper)
    .duration(500)
    .easing('ease-in')
    .fromTo('opacity', '1', '0')
    .fromTo(
      'transform',
      'translateY(0) scale(1) rotate(0deg)',
      'translateY(100vh) scale(0.65) rotate(4deg)'
    );
};

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
    setCurrency,
  } = useContext(ItemContext);

  const { logout, token } = useContext(AuthContext);
  const { isOnline } = useNetwork();

  const [mapOpen, setMapOpen] = React.useState(false);
  const [mapLoading, setMapLoading] = React.useState(false);
  const [mapItems, setMapItems] = React.useState<ItemProps[]>([]);

  const contentRef = useRef<HTMLIonContentElement>(null);
  const checkingRef = useRef(false);

  const pendingIds = new Set(pendingItems.map((entry) => entry.localId));

  useEffect(() => {
    const checkContent = async () => {
      if (checkingRef.current || fetching || loadingMore || !hasMore) {
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

    window.addEventListener("resize", checkContent);

    return () => {
      window.removeEventListener("resize", checkContent);
    };
  }, [items.length, fetching, loadingMore, hasMore, loadMoreItems]);

  log("render", fetching);

  const openAccountsMap = async () => {
    setMapLoading(true);

    try {
      let allItems: ItemProps[] = [];

      if (isOnline && token) {
        let skip = 0;
        let page: ItemProps[];

        do {
          page = await getItems(token, skip, 100);
          allItems = [...allItems, ...page];
          skip += page.length;
        } while (page.length === 100);
      } else {
        allItems = [...items];
      }

      const merged = new Map<string, ItemProps>();

      allItems.forEach((item) => {
        if (item._id) merged.set(item._id, item);
      });

      pendingItems.forEach((entry) => {
        merged.set(entry.item._id || entry.localId, entry.item);
      });

      setMapItems(Array.from(merged.values()));
      setMapOpen(true);
    } catch (error) {
      console.error("Failed to load accounts for map:", error);
      setMapItems(items);
      setMapOpen(true);
    } finally {
      setMapLoading(false);
    }
  };

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonButton
            slot="end"
            fill="clear"
            onClick={async () => {
              await logout?.();
              history.replace("/login");
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
          onIonInput={(event) => setSearch?.(event.detail.value ?? "")}
        />

        <IonButton expand="block" onClick={openAccountsMap}>
          Show all account locations
        </IonButton>

        <IonItem>
          <IonLabel>Filter by currency</IonLabel>
          <IonSelect
            value={currency}
            placeholder="All currencies"
            onIonChange={(event) => setCurrency?.(event.detail.value ?? "")}
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
              {unsentCount} {unsentCount === 1 ? "item" : "items"} not
              synchronized
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
                onEdit={(id) => history.push(`/item/${id}`)}
              />

              {pendingIds.has(_id || "") && (
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
            <IonLabel className="ion-text-center">No accounts found</IonLabel>
          </IonItem>
        )}

        <IonInfiniteScroll
          threshold="100px"
          disabled={!hasMore}
          onIonInfinite={async (event) => {
            await loadMoreItems?.();
            await event.target.complete();
          }}
        >
          <IonInfiniteScrollContent loadingText="Loading more items..." />
        </IonInfiniteScroll>

        {fetchingError && (
          <div>{fetchingError.message || "Failed to fetch items"}</div>
        )}

        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push("/item")}>
            <IonIcon icon={add} />
          </IonFabButton>
        </IonFab>
      </IonContent>

      <IonLoading isOpen={mapLoading} message="Loading account locations..." />
      <IonModal
        isOpen={mapOpen}
        onDidDismiss={() => setMapOpen(false)}
        enterAnimation={mapEnterAnimation}
        leaveAnimation={mapLeaveAnimation}
        style={{ "--height": "90%" }}
      >
        <IonHeader>
          <IonToolbar>
            <IonTitle>Account locations</IonTitle>
            <IonButtons slot="end">
              <IonButton onClick={() => setMapOpen(false)}>Close</IonButton>
            </IonButtons>
          </IonToolbar>
        </IonHeader>

        <IonContent>
          {mapItems.some(
            (item) =>
              typeof item.latitude === "number" &&
              typeof item.longitude === "number",
          ) ? (
            <AccountsMap items={mapItems} />
          ) : (
            <IonItem>
              <IonLabel>No accounts have saved locations yet.</IonLabel>
            </IonItem>
          )}
        </IonContent>
      </IonModal>
    </IonPage>
  );
};

export default ItemList;
