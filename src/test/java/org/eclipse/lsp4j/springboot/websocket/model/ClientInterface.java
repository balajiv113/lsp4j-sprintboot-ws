package org.eclipse.lsp4j.springboot.websocket.model;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;

public interface ClientInterface {

    @JsonNotification("client/notify")
    void notify(String arg);

}
