package org.zdroba.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketNotifier {
    private final int port;
    private final String serverHost;
    private final int serverPort;
    private final List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();
    private PrintWriter out;
    private boolean started = false;

    public SocketNotifier(int port, String serverHost, int serverPort) {
        this.port = port;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void onUpdate(Consumer<String> callback) {
        listeners.add(callback);
    }

    public void start() {
        if (started) return;
        started = true;

        new Thread(() -> {
            try {
                Socket socket = new Socket(serverHost, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("[Port " + port + "] Connected to SyncServer at " + serverHost + ":" + serverPort);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg;
                while ((msg = in.readLine()) != null) {
                    final String received = msg;
                    for (Consumer<String> listener : listeners) {
                        listener.accept(received);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "SocketNotifier-" + port).start();
    }

    public void notifyPeer(String message) {
        if (out != null) {
            System.out.println("[Port " + port + "] sending: " + message);
            out.println(message);
        } else {
            System.err.println("[Port " + port + "] Not connected to server yet");
        }
    }

    public int getPort() {
        return port;
    }

    public void respond(String requestType, ResponseType type, String message) {
        if(out != null) {
            Response response = new Response(type, requestType, message);
            out.println(response.toString());
        } else {
            System.err.println("[Port " + port + "] Not connected to server yet, cannot respond");
        }
    }
}