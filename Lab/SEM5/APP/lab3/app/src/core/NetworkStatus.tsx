import React, { createContext, useContext, useEffect, useState } from 'react';
import { Network } from '@capacitor/network';
import axios from 'axios';

interface NetworkContextType {
  isOnline: boolean;
}

const NetworkContext = createContext<NetworkContextType>({ isOnline: false });

const API_URL = 'http://localhost:3000';

export const NetworkProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isOnline, setIsOnline] = useState(false);

  useEffect(() => {
    let interval: ReturnType<typeof setInterval>;

    const checkServer = async () => {
      const status = await Network.getStatus();

      if (!status.connected) {
        setIsOnline(false);
        return;
      }

      try {
        await axios.get(`${API_URL}/health`, { timeout: 3000 });
        setIsOnline(true);
      } catch {
        setIsOnline(false);
      }
    };

    checkServer();
    interval = setInterval(checkServer, 5000);

    const listener = Network.addListener('networkStatusChange', checkServer);

    return () => {
      clearInterval(interval);
      listener.then(handle => handle.remove());
    };
  }, []);

  return (
    <NetworkContext.Provider value={{ isOnline }}>
      {children}
    </NetworkContext.Provider>
  );
};

export const useNetwork = () => useContext(NetworkContext);