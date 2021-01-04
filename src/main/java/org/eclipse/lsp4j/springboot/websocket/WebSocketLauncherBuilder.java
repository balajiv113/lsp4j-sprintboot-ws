package org.eclipse.lsp4j.springboot.websocket;

import org.eclipse.lsp4j.jsonrpc.Endpoint;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.RemoteEndpoint;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.services.ServiceEndpoints;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

public class WebSocketLauncherBuilder<T> extends Launcher.Builder<T> {
    private WebSocketSession session;
    private MessageJsonHandler jsonHandler;

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public Launcher<T> create() {
        if (localServices == null)
            throw new IllegalStateException("Local service must be configured.");
        if (remoteInterfaces == null)
            throw new IllegalStateException("Remote interface must be configured.");

        this.jsonHandler = createJsonHandler();
        RemoteEndpoint remoteEndpoint = createRemoteEndpoint(jsonHandler);
        T remoteProxy = createProxy(remoteEndpoint);
        return createLauncher(null, remoteProxy, remoteEndpoint, null);
    }

    @Override
    protected RemoteEndpoint createRemoteEndpoint(MessageJsonHandler jsonHandler) {
        MessageConsumer outgoingMessageStream = new WebSocketMessageConsumer(session, jsonHandler);
        outgoingMessageStream = wrapMessageConsumer(outgoingMessageStream);
        Endpoint localEndpoint = ServiceEndpoints.toEndpoint(localServices);
        RemoteEndpoint remoteEndpoint;
        if (exceptionHandler == null)
            remoteEndpoint = new RemoteEndpoint(outgoingMessageStream, localEndpoint);
        else
            remoteEndpoint = new RemoteEndpoint(outgoingMessageStream, localEndpoint, exceptionHandler);
        jsonHandler.setMethodProvider(remoteEndpoint);
        return remoteEndpoint;
    }

    public Collection<Object> getLocalServices() {
        return this.localServices;
    }

    public MessageJsonHandler getJsonHandler() {
        return jsonHandler;
    }
}
