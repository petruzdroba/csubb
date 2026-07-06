package org.zdroba.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zdroba.entity.RaceEvent;
import org.zdroba.service.RaceEventRestService;

import java.io.IOException;
import java.util.Map;

import static org.zdroba.helper.RestEventHelpers.*;

public class RestEventHandler implements HttpHandler {
    private final RaceEventRestService service;

    public RestEventHandler(RaceEventRestService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("OPTIONS")) {
        exchange.sendResponseHeaders(204, -1);
        return;
    }

    String path = exchange.getRequestURI().getPath();
    String query = exchange.getRequestURI().getQuery();

        try {

            if (path.equals("/races")) {
                switch (method) {
                    case "GET" -> getAll(exchange, query);
                    case "POST" -> create(exchange);
                    default -> sendResponse(exchange, 405, "Method Not Allowed");
                }
                return;
            }

            if (path.matches("/races/\\d+")) {
                switch (method) {
                    case "GET" -> getById(exchange);
                    case "PUT" -> update(exchange);
                    case "DELETE" -> delete(exchange);
                    default -> sendResponse(exchange, 405, "Method Not Allowed");
                }
                return;
            }

            sendResponse(exchange, 404, "Not found");

        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void getAll(HttpExchange exchange, String query) throws IOException {
        try {
            var list = service.findAll();

            if (query != null && !query.isEmpty()) {
                Map<String, String> params = parseQueryParams(query);
                if (params.containsKey("engine")) {
                    try {
                        int engineFilter = Integer.parseInt(params.get("engine"));
                        sendResponse(exchange, 200, service.filter(engineFilter).toJson());
                    } catch (NumberFormatException e) {
                        sendResponse(exchange, 400, "Invalid engine parameter");
                        return;
                    }
                }
            }

            String json = "[" +
                    list.stream()
                            .map(RaceEvent::toJson)
                            .collect(java.util.stream.Collectors.joining(",")) +
                    "]";

            sendResponse(exchange, 200, json);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void getById(HttpExchange exchange) throws IOException {
        Long id = extractId(exchange);
        try {
            sendResponse(exchange, 200, service.findById(id).toJson());
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

private void create(HttpExchange exchange) throws IOException {
    try {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Map<String, Object> params = parseJson(body);
        int engine = ((Number) params.get("engine")).intValue();
        sendResponse(exchange, 201, service.save(engine).toJson());
    } catch (NumberFormatException e) {
        sendResponse(exchange, 400, "Invalid engine parameter");
    } catch (Exception e) {
        handleException(exchange, e);
    }
}

private void update(HttpExchange exchange) throws IOException {
    Long id = extractId(exchange);
    try {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Map<String, Object> params = parseJson(body);
        int engine = ((Number) params.get("engine")).intValue();
        sendResponse(exchange, 200, service.update(id, engine).toJson());
    } catch (NumberFormatException e) {
        sendResponse(exchange, 400, "Invalid engine parameter");
    } catch (Exception e) {
        handleException(exchange, e);
    }
}

    private void delete(HttpExchange exchange) throws IOException {
        Long id = extractId(exchange);
        try {
            service.delete(id);
            sendResponse(exchange, 204, "");
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }


    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new java.util.HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }
}
