import { IonBadge } from '@ionic/react';
import { useNetwork } from './NetworkStatus';

const NetworkIndicator: React.FC = () => {
  const { isOnline } = useNetwork();

  return (
    <IonBadge
      color={isOnline ? 'success' : 'danger'}
      style={{
        position: 'fixed',
        bottom: '10px',
        left: '10px',
        zIndex: 9999
      }}
    >
      {isOnline ? 'Online' : 'Offline'}
    </IonBadge>
  );
};

export default NetworkIndicator;