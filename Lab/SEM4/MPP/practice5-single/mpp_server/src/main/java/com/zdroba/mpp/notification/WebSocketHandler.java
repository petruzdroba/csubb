package com.zdroba.mpp.notification;

import com.zdroba.mpp.entity.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        User user = (User) session.getAttributes().get("user");
        sessions.put(user.getId(), session);

        System.out.println("WS: user " + user.getId() + " connected");
        sendToUser(user.getId(), "WS LOGIN OK");
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        User user = (User) session.getAttributes().get("user");

        if (user != null) {
            System.out.println("WS: user " + user.getId() + " disconnected");
        }

        sessions.entrySet().removeIf(
                e -> e.getValue().equals(session)
        );
    }

    public void broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);

        sessions.entrySet().removeIf(e -> !e.getValue().isOpen());

        sessions.forEach((id, session) -> {
            try {
                session.sendMessage(textMessage);
            } catch (Exception e) {
                sessions.remove(id);
            }
        });
    }

    public void sendToUser(Long userId, String message) {
        WebSocketSession session = sessions.get(userId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception ignored) {
            }
        }
    }

    public void sendToUsers(Iterable<Long> userIds, String message) {
        for (Long id : userIds) {
            sendToUser(id, message);
        }
    }
}
