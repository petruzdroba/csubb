package org.zdroba.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RacesClient {

    private static final String BASE = "http://localhost:8080/races";
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        String created = post("8");
        System.out.println(created);

        String id = extractId(created);

        System.out.println(get(""));
        System.out.println(get("/" + id));
        System.out.println(put("/" + id, "16"));
        System.out.println(delete("/" + id));
    }

    static String extractId(String json) {
        Matcher m = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(json);
        if (m.find()) return m.group(1);
        throw new RuntimeException("No id in response: " + json);
    }

    static String get(String path) throws Exception {
        var req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .header("Accept", "application/json")
                .GET()
                .build();

        return send(req, null);
    }

    static String post(String body) throws Exception {
        var req = HttpRequest.newBuilder()
                .uri(URI.create(BASE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(req, body);
    }

    static String put(String path, String body) throws Exception {
        var req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return send(req, body);
    }

    static String delete(String path) throws Exception {
        var req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .DELETE()
                .build();

        return send(req, null);
    }

    static String send(HttpRequest req, String body) throws Exception {
        var res = HTTP.send(req, HttpResponse.BodyHandlers.ofString());
        return LoggingInterceptor.intercept(req, res, body);
    }
}