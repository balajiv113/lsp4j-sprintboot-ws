package org.eclipse.lsp4j.springboot.websocket.model;

import java.util.concurrent.CompletableFuture;

public class MockServer implements ServerInterface {
    ClientInterface client;
    String result = "";

    @Override
    public CompletableFuture<String> request(String arg) {
        return CompletableFuture.supplyAsync(() -> arg + "bar");
    }

    @Override
    public void notify(String arg) {
        this.result += arg;
    }
}
