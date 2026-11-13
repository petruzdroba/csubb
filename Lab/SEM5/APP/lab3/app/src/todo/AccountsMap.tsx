import React, { useEffect } from "react";
import {
  CircleMarker,
  MapContainer,
  TileLayer,
  Tooltip,
  useMap,
} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { ItemProps } from "./ItemProps";
import "./AccountsMap.css";

interface AccountsMapProps {
  items: ItemProps[];
}

const MapBounds: React.FC<{ items: ItemProps[] }> = ({ items }) => {
  const map = useMap();

  useEffect(() => {
    const timer = setTimeout(() => {
      map.invalidateSize();

      const positions: [number, number][] = items.map((item) => [
        item.latitude!,
        item.longitude!,
      ]);

      if (positions.length > 0) {
        map.fitBounds(positions, { padding: [30, 30], maxZoom: 15 });
      }
    }, 100);

    return () => clearTimeout(timer);
  }, [items, map]);

  return null;
};

const AccountsMap: React.FC<AccountsMapProps> = ({ items }) => {
  const locations = items.filter(
    (item) =>
      typeof item.latitude === "number" && typeof item.longitude === "number",
  );

  return (
    <MapContainer
      center={[46.7712, 23.6236]}
      zoom={7}
      style={{
        height: "100%",
        minHeight: "350px",
        width: "100%",
      }}
    >
      <TileLayer
        attribution="&copy; OpenStreetMap contributors"
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {locations.map((item) => (
        <CircleMarker
          key={item._id}
          center={[item.latitude!, item.longitude!]}
          radius={10}
          pathOptions={{ className: "account-marker" }}
        >
          <Tooltip direction="top" permanent>
            <strong>{item.name}</strong>
            <br />
            {item.ammount} {item.currency}
          </Tooltip>
        </CircleMarker>
      ))}

      <MapBounds items={locations} />
    </MapContainer>
  );
};

export default AccountsMap;
