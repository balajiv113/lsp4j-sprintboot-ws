package org.eclipse.lsp4j.springboot.websocket;

import org.eclipse.lsp4j.jsonrpc.JsonRpcException;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.messages.Message;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.logging.Logger;

public class WebSocketMessageConsumer implements MessageConsumer {

    private static final Logger LOG = Logger.getLogger(WebSocketMessageConsumer.class.getName());

    private final WebSocketSession session;
    private final MessageJsonHandler jsonHandler;

    public WebSocketMessageConsumer(WebSocketSession session, MessageJsonHandler jsonHandler) {
        this.session = session;
        this.jsonHandler = jsonHandler;
    }

    public WebSocketSession getSession() {
        return session;
    }

    @Override
    public void consume(Message message) {
        String content = jsonHandler.serialize(message);
        try {
            sendMessage(content);
        } catch (IOException exception) {
            throw new JsonRpcException(exception);
        }
    }

    protected void sendMessage(String message) throws IOException {
        if (session.isOpen()) {
            int length = message.length();
            if (length <= session.getTextMessageSizeLimit()) {
                session.sendMessage(new TextMessage(message, true));
            } else {
                int currentOffset = 0;
                while (currentOffset < length) {
                    int currentEnd = Math.min(currentOffset + session.getTextMessageSizeLimit(), length);
                    session.sendMessage(new TextMessage(message.substring(currentOffset, currentEnd), currentEnd == length));
                    currentOffset = currentEnd;
                }
            }
        } else {
            LOG.info("Ignoring message due to closed session: " + message);
        }
    }
}
