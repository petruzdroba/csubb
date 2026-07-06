package org.zdroba;

import com.sun.net.httpserver.HttpServer;
import org.zdroba.handler.RestEventHandler;
import org.zdroba.repository.RaceEventRepositoryImpl;
import org.zdroba.service.RaceEventRestService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestApp {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException("Port required: java RestApp <port>");
        }

        int port = Integer.parseInt(args[0]);

        var repo = new RaceEventRepositoryImpl();
        var service = new RaceEventRestService(repo);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/races", new RestEventHandler(service));

        server.setExecutor(null);
        server.start();

        System.out.println("REST server running on port " + port);
    }
}