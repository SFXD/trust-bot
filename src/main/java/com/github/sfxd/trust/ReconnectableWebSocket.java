package com.github.sfxd.trust;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/** A websocket that will reconnect itself after the close event is sent. */
@ApplicationScoped
public class ReconnectableWebSocket implements WebSocket {

    private final URI uri;
    private final HttpClient client;
    private final WebSocket.Listener listener;

    // TODO: this should probably be a dependency passed in for testing
    private final ReentrantReadWriteLock wsLock = new ReentrantReadWriteLock();
    private volatile WebSocket webSocket;

    @Inject
    public ReconnectableWebSocket(URI uri, HttpClient client, WebSocket.Listener listener) {
        Objects.requireNonNull(uri);
        Objects.requireNonNull(client);
        Objects.requireNonNull(listener);

        this.uri = uri;
        this.client = client;
        this.listener = listener;
        this.webSocket = this.client.newWebSocketBuilder().buildAsync(this.uri, this.listener).join();
    }

    private <T> T withReadLock(Function<WebSocket, T> fn) {
        return withLock(this.wsLock.readLock(), this.webSocket, fn);
    }

    private <T> T withWriteLock(Function<WebSocket, T> fn) {
        return withLock(this.wsLock.writeLock(), this.webSocket, fn);
    }

    private static <T> T withLock(Lock l, WebSocket ws, Function<WebSocket, T> fn) {
        l.lock();
        try {
            return fn.apply(ws);
        } finally {
            l.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
        return this.withReadLock(ws -> ws.sendText(data, last));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last) {
        return this.withReadLock(ws -> ws.sendBinary(data, last));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<WebSocket> sendPing(ByteBuffer message) {
        return this.withReadLock(ws -> ws.sendPing(message));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<WebSocket> sendPong(ByteBuffer message) {
        return this.withReadLock(ws -> ws.sendPong(message));
    }

    /** {@inheritDoc} */
    @Override
    public CompletableFuture<WebSocket> sendClose(int statusCode, String reason) {
        return this.withReadLock(ws -> ws.sendClose(statusCode, reason)).thenCompose(ws -> {
            return this.client.newWebSocketBuilder().buildAsync(this.uri, this.listener).thenApply(newWs -> {
                return this.withWriteLock(old -> {
                    this.webSocket = newWs;
                    return this.webSocket;
                });
            });
        });
    }

    /** {@inheritDoc} */
    @Override
    public void request(long n) {
        this.withReadLock(ws -> {
            ws.request(n);
            return null;
        });
    }

    /** {@inheritDoc} */
    @Override
    public String getSubprotocol() {
        return this.withReadLock(WebSocket::getSubprotocol);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOutputClosed() {
        return this.withReadLock(WebSocket::isOutputClosed);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInputClosed() {
        return this.withReadLock(WebSocket::isInputClosed);
    }

    /** {@inheritDoc} */
    @Override
    public void abort() {
        this.withWriteLock(ws -> {
            ws.abort();
            return null;
        });
    }
}
