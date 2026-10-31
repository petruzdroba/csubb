import React, { useContext, useEffect, useState } from "react";
import {
  IonButton,
  IonButtons,
  IonContent,
  IonDatetime,
  IonHeader,
  IonInput,
  IonLabel,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
} from "@ionic/react";
import { getLogger } from "../core";
import { ItemContext } from "./ItemProvider";
import { RouteComponentProps } from "react-router";
import { ItemProps } from "./ItemProps";

const log = getLogger("ItemEdit");

interface ItemEditProps extends RouteComponentProps<{
  id?: string;
}> {}

const ItemEdit: React.FC<ItemEditProps> = ({ history, match }) => {
  const { items, saving, savingError, saveItem } = useContext(ItemContext);
  const [name, setName] = useState("");
  const [currency, setCurrency] = useState<string>("");
  const [ammount, setAmmount] = useState<number>(0);
  const [date_opened, setDateOpened] = useState<Date>(new Date());
  const [item, setItem] = useState<ItemProps>();
  useEffect(() => {
    log("useEffect");
    const routeId = match.params.id || "";
    const item = items?.find((it) => it._id === routeId);
    setItem(item);
    if (item) {
      setName(item.name);
      setCurrency(item.currency);
      setAmmount(item.ammount);
      setDateOpened(item.date_opened);
    }
  }, [match.params.id, items]);
  const handleSave = () => {
    const editedItem = item
      ? { ...item, name, currency, ammount, date_opened }
      : { name, currency, ammount, date_opened };
    saveItem && saveItem(editedItem).then(() => history.goBack());
  };
  log("render");
  return (
    <IonPage>
      <IonHeader>
        <IonToolbar>
          <IonTitle>Edit</IonTitle>
          <IonButtons slot="end">
            <IonButton onClick={handleSave}>Save</IonButton>
          </IonButtons>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonLabel>Name of account</IonLabel>
        <IonInput
          value={name}
          onIonChange={(e) => setName(e.detail.value || "")}
        />
        <IonLabel>Currency</IonLabel>
        <IonInput
          value={currency}
          onIonChange={(e) => setCurrency(e.detail.value || "")}
        />
        <IonLabel>Ammount</IonLabel>
        <IonInput
          value={ammount}
          type="number"
          onIonChange={(e) =>
            setAmmount(parseInt(e.detail.value as string) || 0)
          }
        />

        <IonDatetime
          value={date_opened.toString()}
          onIonChange={(e) => setDateOpened((new Date(e.detail.value as string)) || new Date())}
        />

        <IonLoading isOpen={saving} />
        {savingError && (
          <div>{savingError.message || "Failed to save item"}</div>
        )}
      </IonContent>
    </IonPage>
  );
};

export default ItemEdit;
