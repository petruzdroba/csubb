package org.zdroba.sync;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType type;
    private final String requestType;
    private final String message;

    public Response(ResponseType type, String requestType, String message) {
        this.type = type;
        this.requestType = requestType;
        this.message = message;
    }

    public ResponseType getType() {
        return type;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RESPONSE:" + type + "|" + requestType + "|" + (message != null ? message : "");
    }

    public static Response fromString(String str) {
        if (!str.startsWith("RESPONSE:"))
            throw new IllegalArgumentException("Not a response string");

        String[] parts = str.substring(9).split("\\|", 3);
        ResponseType type = ResponseType.valueOf(parts[0]);
        String requestType = parts[1];
        String message = parts.length > 2 ? parts[2] : null;

        return new Response(type, requestType, message);
    }
}