package org.eclipse.lsp4j.springboot.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ExecutionException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = WebSocketTestConfiguration.class)
public class MockConnectionTest {

    @LocalServerPort
    String serverPort;
    WebSocketSession session;

    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {
        WebSocketClient server = new StandardWebSocketClient();
        session = server.doHandshake(new TextWebSocketHandler(), "ws://localhost:" + serverPort + "services/server").get();
    }


}
