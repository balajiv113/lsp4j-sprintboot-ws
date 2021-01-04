package org.eclipse.lsp4j.springboot.websocket.model;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;

import java.util.concurrent.CompletableFuture;

public interface ServerInterface {

    @JsonRequest("server/request")
    CompletableFuture<String> request(String arg);

    @JsonNotification("server/notify")
    void notify(String arg);

}
