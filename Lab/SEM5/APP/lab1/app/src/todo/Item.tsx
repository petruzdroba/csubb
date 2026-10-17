import React from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import { ItemProps } from './ItemProps';

interface ItemPropsExt extends ItemProps {
  onEdit: (_id?: string) => void;
}

const Item: React.FC<ItemPropsExt> = ({ _id, name, currency, date_opened, ammount,  onEdit }) => {
  return (
    <IonItem onClick={() => onEdit(_id)}>
      <IonLabel>{name}</IonLabel>
      <IonLabel>{date_opened.toString().substring(0,10)}</IonLabel>
      <IonLabel>{ammount}</IonLabel>
      <IonLabel>{currency}</IonLabel>
    </IonItem>
  );
};

export default Item;
