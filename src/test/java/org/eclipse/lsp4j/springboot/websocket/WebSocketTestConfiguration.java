package org.eclipse.lsp4j.springboot.websocket;

import org.eclipse.lsp4j.springboot.websocket.model.ClientInterface;
import org.eclipse.lsp4j.springboot.websocket.model.MockClient;
import org.eclipse.lsp4j.springboot.websocket.model.MockServer;
import org.eclipse.lsp4j.springboot.websocket.model.ServerInterface;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@TestConfiguration
public class WebSocketTestConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler<ServerInterface>() {
            @Override
            void configure(WebSocketLauncherBuilder<ServerInterface> builder) {
                builder
                        .setLocalService(new MockClient())
                        .setRemoteInterface(ServerInterface.class);
            }
        }, "services/server");

        registry.addHandler(new WebSocketHandler<ClientInterface>() {
            @Override
            void configure(WebSocketLauncherBuilder<ClientInterface> builder) {
                builder
                        .setLocalService(new MockServer())
                        .setRemoteInterface(ClientInterface.class);
            }
        }, "services/client");
    }
}
