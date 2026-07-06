package org.zdroba.sync;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SyncServer {
    private static SyncServer instance;
    private final int port;
    private final List<PrintWriter> clients = new CopyOnWriteArrayList<>();

    private SyncServer(int port) {
        this.port = port;
    }

    public static synchronized SyncServer getInstance(int port) {
        if (instance == null) {
            instance = new SyncServer(port);
        }
        return instance;
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("SyncServer started on port " + port);
                while (true) {
                    Socket socket = server.accept();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    clients.add(out);
                    System.out.println("Client connected. Total: " + clients.size());
                    System.out.flush();

                    new Thread(() -> {
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String msg;
                            while ((msg = in.readLine()) != null) {
                                broadcast(msg, out);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            clients.remove(out);
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "SyncServer").start();
    }

    private void broadcast(String message, PrintWriter sender) {
        for (PrintWriter client : clients) {
            if (client != sender) {
                client.println(message);
            }
        }
    }
}
