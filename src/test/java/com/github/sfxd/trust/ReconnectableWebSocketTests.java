package com.github.sfxd.trust;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

class ReconnectableWebSocketTests {

    @Test
    void it_should_forward_all_calls_to_the_inner_websocket() {
        var uri = URI.create("https://localhost:8080");
        var client = mock(HttpClient.class);
        var listener = mock(WebSocket.Listener.class);
        var builder = mock(WebSocket.Builder.class);
        var websocket = mock(WebSocket.class);

        when(client.newWebSocketBuilder()).thenReturn(builder);
        when(builder.buildAsync(uri, listener)).thenReturn(CompletableFuture.completedFuture(websocket));

        var socket = new ReconnectableWebSocket(uri, client, listener);
        var data = "hello";
        var last = true;

        socket.sendText(data, last);
        verify(websocket).sendText(data, last);

        socket.sendBinary(null, last);
        verify(websocket).sendBinary(null, last);

        socket.sendPing(null);
        verify(websocket).sendPing(null);

        socket.sendPong(null);
        verify(websocket).sendPong(null);

        long n = 5L;
        socket.request(n);
        verify(websocket).request(n);

        socket.getSubprotocol();
        verify(websocket).getSubprotocol();

        socket.isOutputClosed();
        verify(websocket).isOutputClosed();

        socket.isInputClosed();
        verify(websocket).isInputClosed();

        socket.abort();
        verify(websocket).abort();
    }

    @Test
    void it_should_reconnect_after_sending_close() {
        var uri = URI.create("https://localhost:8080");
        var client = mock(HttpClient.class);
        var listener = mock(WebSocket.Listener.class);
        var builder = mock(WebSocket.Builder.class);
        var websocket = mock(WebSocket.class);

        when(client.newWebSocketBuilder()).thenReturn(builder);
        when(builder.buildAsync(uri, listener)).thenReturn(CompletableFuture.completedFuture(websocket));

        var closeCode = 4000;
        var reason = "";
        when(websocket.sendClose(closeCode, reason)).thenReturn(CompletableFuture.completedFuture(websocket));

        var socket = new ReconnectableWebSocket(uri, client, listener);
        socket.sendClose(closeCode, reason);
        verify(websocket).sendClose(closeCode, reason);
        verify(builder, times(2)).buildAsync(uri, listener);
    }
}
