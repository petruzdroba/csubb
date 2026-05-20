package org.zdroba.sync;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketNotifier {
    private final int port;
    private final String serverHost;
    private final int serverPort;
    private final List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();
    private SyncServiceGrpc.SyncServiceStub asyncStub;
    private SyncServiceGrpc.SyncServiceBlockingStub blockingStub;
    private final String clientId = UUID.randomUUID().toString();
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

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverHost, serverPort)
                .usePlaintext()
                .build();

        asyncStub = SyncServiceGrpc.newStub(channel);
        blockingStub = SyncServiceGrpc.newBlockingStub(channel);

        SyncProto.SubscribeRequest request = SyncProto.SubscribeRequest.newBuilder()
                .setClientId(clientId)
                .build();

        asyncStub.subscribe(request, new StreamObserver<SyncProto.SyncMessage>() {
            @Override
            public void onNext(SyncProto.SyncMessage message) {
                String payload = message.getPayload();
                System.out.println("[Port " + port + "] received: " + payload);
                for (Consumer<String> listener : listeners) {
                    listener.accept(payload);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("[Port " + port + "] Stream error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("[Port " + port + "] Stream closed");
            }
        });

        System.out.println("[Port " + port + "] Connected to gRPC SyncServer at " + serverHost + ":" + serverPort);
    }

public void notifyPeer(String message) {
    if (blockingStub == null) {
        System.err.println("[Port " + port + "] Not connected yet");
        return;
    }
    SyncProto.SyncMessage msg = SyncProto.SyncMessage.newBuilder()
            .setPayload(message)
            .build();
    blockingStub.notify(msg);
    System.out.println("[Port " + port + "] sent: " + message);
}

    public void respond(String requestType, ResponseType type, String message) {
        Response response = new Response(type, requestType, message);
        notifyPeer(response.toString());
    }

    public int getPort() {
        return port;
    }
}
