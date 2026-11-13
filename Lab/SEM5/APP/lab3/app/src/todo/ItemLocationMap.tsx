
import React, { useEffect } from 'react';
import { CircleMarker, MapContainer, TileLayer, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

interface ItemLocationMapProps {
  latitude: number;
  longitude: number;
}

const MapResize: React.FC = () => {
  const map = useMap();

  useEffect(() => {
    const timer = setTimeout(() => map.invalidateSize(), 100);
    return () => clearTimeout(timer);
  }, [map]);

  return null;
};

const ItemLocationMap: React.FC<ItemLocationMapProps> = ({ latitude, longitude }) => {
  const position: [number, number] = [latitude, longitude];

  return (
    <MapContainer
      center={position}
      zoom={15}
      style={{ height: '100%', width: '100%' }}
    >
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <CircleMarker center={position} radius={10} />
      <MapResize />
    </MapContainer>
  );
};

export default ItemLocationMap;
