import React, { useEffect, useState } from 'react';
import { IonItem, IonLabel, IonAvatar, IonImg } from '@ionic/react';
import { ItemProps } from './ItemProps';
import { loadAvatar } from './avatarStorage';

interface ItemPropsExt extends ItemProps {
  onEdit: (_id?: string) => void;
}

const Item: React.FC<ItemPropsExt> = ({
  _id, name, date_opened, ammount, currency, onEdit
}) => {
  const [avatar, setAvatar] = useState<string>();

  useEffect(() => {
    if (_id) {
      loadAvatar(_id).then(setAvatar);
    }
  }, [_id]);

  return (
    <IonItem button onClick={() => onEdit(_id)}>
      {avatar && (
        <IonAvatar slot="start">
          <IonImg src={avatar} />
        </IonAvatar>
      )}
      <IonLabel>{name}</IonLabel>
      <IonLabel>{date_opened.toString().substring(0, 10)}</IonLabel>
      <IonLabel>{ammount}</IonLabel>
      <IonLabel>{currency}</IonLabel>
    </IonItem>
  );
};

export default Item;
