package org.zdroba;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java ClientMain <clientPort>");
            System.exit(1);
        }

        int clientPort = Integer.parseInt(args[0]);

        App.CLIENT_PORT = clientPort;
        Application.launch(App.class, args);
    }
}