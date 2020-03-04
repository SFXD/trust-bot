package com.github.sfxd.trust;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

/** Handler for a deserialized message from the gateway. */
public interface GatewayMessageHandler {
    /**
     * Handles the message from the gateway for a particular opcode. This should be
     * a CompletionStage for when the websocket can free the related data or null if
     * it can free the data immediately.
     *
     * @param webSocket the websocket from the listener that called the handler
     * @param data      the deserialized data from the listener
     * @return Implementors should return a {@link CompletionStage} that completes
     *         when the websocket listener can safely free the data or null if it
     *         can immediately free it.
     * @see WebSocket.Listener#onText(WebSocket, CharSequence, boolean)
     */
    CompletionStage<?> handle(WebSocket webSocket, JsonNode data);
}
