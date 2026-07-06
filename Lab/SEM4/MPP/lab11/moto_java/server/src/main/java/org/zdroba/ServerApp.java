package org.zdroba;

import org.zdroba.sync.SyncServer;

public class ServerApp {
    public static void main(String[] args) throws InterruptedException {
        SyncServer.getInstance(6000).start();
        System.out.println("Server running on port 6000...");
        Thread.currentThread().join();
    }
}