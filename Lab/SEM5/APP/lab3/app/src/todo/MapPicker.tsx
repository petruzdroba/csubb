import React, { useEffect } from 'react';
import { CircleMarker, MapContainer, TileLayer, useMap, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

interface MapPickerProps {
  latitude?: number;
  longitude?: number;
  onSelect: (latitude: number, longitude: number) => void;
}

const MapEvents: React.FC<{ onSelect: MapPickerProps['onSelect'] }> = ({ onSelect }) => {
  useMapEvents({
    click(event) {
      onSelect(event.latlng.lat, event.latlng.lng);
    }
  });

  return null;
};

const MapResize: React.FC = () => {
  const map = useMap();

  useEffect(() => {
    setTimeout(() => map.invalidateSize(), 100);
  }, [map]);

  return null;
};

const MapPicker: React.FC<MapPickerProps> = ({ latitude, longitude, onSelect }) => {
  const position: [number, number] =
    latitude !== undefined && longitude !== undefined
      ? [latitude, longitude]
      : [46.7712, 23.6236];

  return (
    <MapContainer
      center={position}
      zoom={latitude !== undefined ? 15 : 6}
      style={{ height: '400px', width: '100%' }}
    >
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <MapEvents onSelect={onSelect} />
      <MapResize />
      {latitude !== undefined && longitude !== undefined && (
        <CircleMarker center={position} radius={9} />
      )}
    </MapContainer>
  );
};

export default MapPicker;
