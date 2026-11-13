import WebSocket from "ws";
import jwt from "jsonwebtoken";
import { jwtConfig, revokedTokens } from "./utils.js";

let wss;

export const initWss = value => {
  wss = value;

  wss.on('connection', ws => {
    ws.user = null;

    ws.on('message', message => {
      try {
        const { type, payload } = JSON.parse(message.toString());

        if (type !== 'authorization' || !payload?.token || ws.user) {
          ws.close();
          return;
        }

        const token = payload.token;

        if (revokedTokens.has(token)) {
          ws.close();
          return;
        }

        ws.user = jwt.verify(token, jwtConfig.secret);

        if (!ws.user._id) {
          ws.close();
          return;
        }
      } catch (err) {
        ws.close();
      }
    });
  });
};

export const broadcast = (userId, data) => {
  if (!wss) {
    return;
  }

  wss.clients.forEach(client => {
    if (
      client.readyState === WebSocket.OPEN &&
      client.user?._id === userId
    ) {
      console.log(`broadcast sent to ${client.user.username}`);
      client.send(JSON.stringify(data));
    }
  });
};