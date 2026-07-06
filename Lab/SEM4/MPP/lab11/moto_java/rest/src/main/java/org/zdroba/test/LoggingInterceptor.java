package org.zdroba.test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoggingInterceptor {

    public static String intercept(HttpRequest req, HttpResponse<String> res, String body) {
        StringBuilder out = new StringBuilder();

        out.append(req.method())
                .append(" ")
                .append(req.uri())
                .append("\n");

        req.headers().map().forEach((k, v) ->
                out.append(k)
                        .append(": ")
                        .append(String.join(", ", v))
                        .append("\n")
        );

        if (body != null && !body.isBlank()) {
            out.append("\n")
                    .append("Request Body:\n")
                    .append(body)
                    .append("\n");
        }

        out.append("\n")
                .append("Sending request...\n\n")
                .append("Response Status:\n")
                .append(res.statusCode())
                .append("\n\n")
                .append("Response Body:\n")
                .append(res.body())
                .append("\n");

        return out.toString();
    }
}