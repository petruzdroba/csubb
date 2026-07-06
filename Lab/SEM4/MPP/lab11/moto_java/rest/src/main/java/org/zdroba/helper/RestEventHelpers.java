package org.zdroba.helper;

import com.sun.net.httpserver.HttpExchange;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidEngineException;
import org.zdroba.exceptions.NotFoundException;
import java.util.Map;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

public class RestEventHelpers {

    static public Long extractId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        return Long.parseLong(path.split("/")[2]);
    }

    static public int parseBody(HttpExchange exchange) throws IOException {
        return Integer.parseInt(new String(exchange.getRequestBody().readAllBytes()).trim());
    }

    public static Map<String, Object> parseJson(String json) {
    Map<String, Object> map = new java.util.HashMap<>();
    
    json = json.trim();
    if (json.startsWith("{")) {
        json = json.substring(1);
    }
    if (json.endsWith("}")) {
        json = json.substring(0, json.length() - 1);
    }
    
    String[] pairs = json.split(",");
    
    for (String pair : pairs) {
        pair = pair.trim();
        int colonIndex = pair.indexOf(":");
        
        if (colonIndex > 0) {
            String key = pair.substring(0, colonIndex).trim();
            String value = pair.substring(colonIndex + 1).trim();
            
            key = key.replaceAll("^\"|\"$", "");
            
            value = value.trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                map.put(key, value.substring(1, value.length() - 1));
            } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                map.put(key, Boolean.parseBoolean(value));
            } else {
                try {
                    if (value.contains(".")) {
                        map.put(key, Double.parseDouble(value));
                    } else {
                        map.put(key, Long.parseLong(value));
                    }
                } catch (NumberFormatException e) {
                    map.put(key, value);
                }
            }
        }
    }
    
    return map;
}

public static void sendResponse(HttpExchange ex, int status, String body) throws IOException {
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    ex.getResponseHeaders().set("Content-Type", "application/json");
    ex.sendResponseHeaders(status, bytes.length);
    try (OutputStream os = ex.getResponseBody()) {
        os.write(bytes);
    }
}


    static public String route(String path) {
        if (path.matches("/races/\\d+")) return "/races/{id}";
        return path;
    }

static public void handleException(HttpExchange ex, Exception e) throws IOException {
    String body = "{\"error\": \"" + e.getMessage() + "\"}";
    
    if (e instanceof NotFoundException) {
        sendResponse(ex, 404, body);
        return;
    }
    if (e instanceof AlreadyExistsException) {
        sendResponse(ex, 409, body);
        return;
    }
    if (e instanceof InvalidEngineException) {
        sendResponse(ex, 400, body);
        return;
    }
    sendResponse(ex, 500, "{\"error\": \"Internal server error\"}");
}

}
