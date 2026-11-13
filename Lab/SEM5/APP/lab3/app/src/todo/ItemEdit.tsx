import React, { useContext, useEffect, useRef, useState } from "react";
import {
  IonButton,
  IonButtons,
  IonContent,
  IonDatetime,
  IonHeader,
  IonImg,
  IonModal,
  IonInput,
  IonLabel,
  IonLoading,
  IonPage,
  IonTitle,
  IonToolbar,
  IonSelect,
  IonSelectOption,
} from "@ionic/react";
import { Camera, CameraResultType, CameraSource } from "@capacitor/camera";
import { getLogger } from "../core";
import { ItemContext } from "./ItemProvider";
import { RouteComponentProps } from "react-router";
import { ItemProps } from "./ItemProps";
import { uploadAvatar } from './itemApi';
import { saveAvatar, loadAvatar } from './avatarStorage';
import { AuthContext } from '../auth/AuthProvider';
import { useNetwork } from '../core/NetworkStatus';
import MapPicker from './MapPicker';

const log = getLogger("ItemEdit");

interface ItemEditProps extends RouteComponentProps<{
  id?: string;
}> {}

const ItemEdit: React.FC<ItemEditProps> = ({ history, match }) => {
  const { items, saving, savingError, saveItem } = useContext(ItemContext);

  const [name, setName] = useState("");
  const [currency, setCurrency] = useState<string>("RON");
  const [ammount, setAmmount] = useState<number>(0);
  const [date_opened, setDateOpened] = useState<Date>(new Date());
  const [item, setItem] = useState<ItemProps>();
  const [photo, setPhoto] = useState<string>();

  const videoRef = useRef<HTMLVideoElement>(null);
  const streamRef = useRef<MediaStream | null>(null);
  const [cameraOpen, setCameraOpen] = useState(false);

  const { token } = useContext(AuthContext);
  const { isOnline } = useNetwork();

  const [latitude, setLatitude] = useState<number>();
  const [longitude, setLongitude] = useState<number>();
  const [mapOpen, setMapOpen] = useState(false);

  const startCamera = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: true,
        audio: false,
      });

      streamRef.current = stream;
      setCameraOpen(true);

      setTimeout(() => {
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
        }
      }, 100);
    } catch (error) {
      console.error("Webcam error:", error);
      alert(`Could not access webcam: ${String(error)}`);
    }
  };

  const capturePhoto = () => {
    const video = videoRef.current;
    if (!video) return;

    const canvas = document.createElement("canvas");
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const context = canvas.getContext("2d");
    if (!context) return;

    context.drawImage(video, 0, 0);

    setPhoto(canvas.toDataURL("image/jpeg", 0.8));
    stopCamera();
  };

  const stopCamera = () => {
    streamRef.current?.getTracks().forEach(track => track.stop());
    streamRef.current = null;
    setCameraOpen(false);
  };

  useEffect(() => {
    log("useEffect");

    const routeId = match.params.id || "";
    const foundItem = items?.find((it) => it._id === routeId);

    setItem(foundItem);

    if (foundItem) {
      setName(foundItem.name);
      setCurrency(foundItem.currency);
      setAmmount(foundItem.ammount);
      setDateOpened(foundItem.date_opened);
      setLatitude(foundItem.latitude);
      setLongitude(foundItem.longitude);
    }
  }, [match.params.id, items]);

  useEffect(() => {
    if (item?._id) {
      loadAvatar(item._id).then(savedPhoto => {
        if (savedPhoto) setPhoto(savedPhoto);
      });
    }
  }, [item?._id]);

  const takePhoto = async () => {
    try {
      const result = await Camera.getPhoto({
        quality: 80,
        allowEditing: true,
        resultType: CameraResultType.DataUrl,
        source: CameraSource.Photos,
      });

      if (result.dataUrl) {
        setPhoto(result.dataUrl);
      }
    } catch (error) {
      console.error("Camera error:", error);
      alert(`Camera error: ${String(error)}`);
    }
  };

  const handleSave = async () => {
    const editedItem = item
      ? { ...item, name, currency, ammount, date_opened, latitude, longitude }
      : { name, currency, ammount, date_opened, latitude, longitude };

    try {
      const savedItem = await saveItem?.(editedItem);

      if (!savedItem?._id) return;

      if (photo) {
        await saveAvatar(savedItem._id, photo);

        if (isOnline && token) {
          const result = await uploadAvatar(token, savedItem._id, photo);
          console.log('Avatar uploaded:', result.avatarUrl);
        }
      }

      history.goBack();
    } catch (error) {
      console.error('Failed to save account or upload avatar:', error);
    }
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

      <IonContent className="ion-padding">
        <IonLabel>Account picture</IonLabel>
        {photo && (
          <IonImg
            src={photo}
            style={{
              width: "150px",
              height: "150px",
              objectFit: "cover",
              margin: "10px auto",
              borderRadius: "50%",
              overflow: "hidden",
            }}
          />
        )}

        {cameraOpen && (
          <div>
            <video
              ref={videoRef}
              autoPlay
              playsInline
              style={{ width: "100%", maxWidth: "400px" }}
            />

            <IonButton expand="block" onClick={capturePhoto}>
              Capture photo
            </IonButton>

            <IonButton expand="block" color="medium" onClick={stopCamera}>
              Cancel
            </IonButton>
          </div>
        )}

        {!cameraOpen && (
          <IonButton expand="block" onClick={startCamera}>
            {photo ? "Take another picture" : "Open webcam"}
          </IonButton>
        )}

        <IonLabel>Name of account</IonLabel>
        <IonInput
          value={name}
          onIonChange={(e) => setName(e.detail.value || "")}
        />

        <IonLabel>Currency</IonLabel>
        <IonSelect
          value={currency}
          placeholder="Select currency"
          interface="popover"
          onIonChange={(e) => setCurrency(e.detail.value)}
        >
          <IonSelectOption value="RON">RON</IonSelectOption>
          <IonSelectOption value="EUR">EUR</IonSelectOption>
          <IonSelectOption value="USD">USD</IonSelectOption>
          <IonSelectOption value="GBP">GBP</IonSelectOption>
          <IonSelectOption value="CAD">CAD</IonSelectOption>
          <IonSelectOption value="AUD">AUD</IonSelectOption>
        </IonSelect>

        <IonLabel>Amount</IonLabel>
        <IonInput
          value={ammount}
          type="number"
          onIonChange={(e) =>
            setAmmount(parseInt(e.detail.value as string) || 0)
          }
        />

        <IonDatetime
          value={date_opened.toString()}
          onIonChange={(e) =>
            setDateOpened(new Date(e.detail.value as string) || new Date())
          }
        />

        <IonButton expand="block" onClick={() => setMapOpen(true)}>
          {latitude !== undefined ? 'Change location' : 'Select location'}
        </IonButton>

        {latitude !== undefined && longitude !== undefined && (
          <IonLabel>
            Selected: {latitude.toFixed(5)}, {longitude.toFixed(5)}
          </IonLabel>
        )}

        <IonModal isOpen={mapOpen} onDidDismiss={() => setMapOpen(false)}>
          <IonHeader>
            <IonToolbar>
              <IonTitle>Select account location</IonTitle>
              <IonButtons slot="end">
                <IonButton onClick={() => setMapOpen(false)}>Done</IonButton>
              </IonButtons>
            </IonToolbar>
          </IonHeader>
          <IonContent>
            <MapPicker
              latitude={latitude}
              longitude={longitude}
              onSelect={(lat, lng) => {
                setLatitude(lat);
                setLongitude(lng);
              }}
            />
          </IonContent>
        </IonModal>

        <IonLoading isOpen={saving} />
        {savingError && (
          <div>{savingError.message || "Failed to save item"}</div>
        )}
      </IonContent>
    </IonPage>
  );
};

export default ItemEdit;
