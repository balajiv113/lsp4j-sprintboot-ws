package org.eclipse.lsp4j.springboot.websocket;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.springframework.lang.NonNullApi;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collection;

public abstract class WebSocketHandler<T> extends TextWebSocketHandler {
    private StringBuilder currentStream = new StringBuilder();
    private MessageJsonHandler jsonHandler;
    private Launcher<T> launcher;

    abstract void configure(WebSocketLauncherBuilder<T> builder);

    protected void connect(Collection<Object> localServices, T remoteProxy) {
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        super.afterConnectionEstablished(webSocketSession);
        WebSocketLauncherBuilder<T> builder = new WebSocketLauncherBuilder<>();
        builder.setSession(webSocketSession);
        configure(builder);
        this.launcher = builder.create();
        this.jsonHandler = builder.getJsonHandler();
        connect(builder.getLocalServices(), this.launcher.getRemoteProxy());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if (message.isLast()) {
            currentStream.append(message.getPayload());
            Message parseMessage = jsonHandler.parseMessage(currentStream.toString());
            this.launcher.getRemoteEndpoint().consume(parseMessage);
            currentStream = new StringBuilder();
        } else {
            currentStream.append(message.getPayload());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
