package org.zdroba.notification;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RaceWebSocketHandler handler;
    private final JwtHandshakeInterceptor interceptor;

    public WebSocketConfig(RaceWebSocketHandler handler, JwtHandshakeInterceptor interceptor) {
        this.handler = handler;
        this.interceptor = interceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/races")
                .addInterceptors(interceptor)
                .setAllowedOrigins("http://localhost:4200");
    }
}
