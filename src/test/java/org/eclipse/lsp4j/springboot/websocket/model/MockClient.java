package org.eclipse.lsp4j.springboot.websocket.model;

public class MockClient implements ClientInterface {
    MockServer server;
    String result = "";

    @Override
    public void notify(String arg) {
        this.result += arg;
    }
}