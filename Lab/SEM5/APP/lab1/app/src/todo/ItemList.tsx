import React, { useContext } from 'react';
import { RouteComponentProps } from 'react-router';
import {
  IonContent,
  IonFab,
  IonFabButton,
  IonHeader,
  IonIcon,
  IonItem,
  IonLabel,
  IonList, IonLoading,
  IonPage,
  IonTitle,
  IonToolbar
} from '@ionic/react';
import { add } from 'ionicons/icons';
import Item from './Item';
import { getLogger } from '../core';
import { ItemContext } from './ItemProvider';

const log = getLogger('ItemList');

const ItemList: React.FC<RouteComponentProps> = ({ history }) => {
  const { items, fetching, fetchingError } = useContext(ItemContext);
  log('render', fetching);
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Bank accounts</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonLoading isOpen={fetching} message="Fetching items"/>
        <IonItem>
          <IonLabel>Name</IonLabel>
          <IonLabel>Date opened</IonLabel>
          <IonLabel>Ammount</IonLabel>
          <IonLabel>Currency</IonLabel>
        </IonItem>
        {items && (
          <IonList>
            {items.map(({ _id, name, currency, date_opened ,ammount }) =>
              <Item key={_id} _id={_id} name={name} currency={currency} date_opened={date_opened} ammount={ammount} onEdit={id => history.push(`/item/${id}`)}/>)}
          </IonList>
        )}
        {fetchingError && (
          <div>{fetchingError.message || 'Failed to fetch items'}</div>
        )}
        <IonFab vertical="bottom" horizontal="end" slot="fixed">
          <IonFabButton onClick={() => history.push('/item')}>
            <IonIcon icon={add}/>
          </IonFabButton>
        </IonFab>
      </IonContent>
    </IonPage>
  );
};

export default ItemList;
