package org.zdroba.sync;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private String message;

    public Request(RequestType type, String message) {
        this.type = type;
        this.message = message;
    }

    public RequestType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return type + "|" + message;
    }

    public static Request fromString(String str) {
        String[] parts = str.split("\\|", 2);
        if (parts.length != 2) throw new IllegalArgumentException("Invalid request: " + str);
        return new Request(RequestType.valueOf(parts[0]), parts[1]);
    }
}