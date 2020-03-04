package com.github.sfxd.trust;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main listener for the discord gateway api. This listener directly
 * handles the heartbeat response and resuming the connection to the api.
 * All other opcodes not related to the heartbeat are sent to their
 * respective handler.
 */
@ApplicationScoped
public class GatewayWebSocketListener implements WebSocket.Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayWebSocketListener.class);
    private static final String OP = "op";
    private static final String S = "s";
    private static final String SFXD_TRUST_BOT = "SFXD_TRUST_BOT";

    private final AtomicInteger sequence = new AtomicInteger(0);
    private final AtomicBoolean ack = new AtomicBoolean();
    private final ScheduledExecutorService scheduler;
    private final ObjectMapper mapper;
    private final String token;
    private final AtomicReference<ScheduledFuture<?>> heartbeat = new AtomicReference<>();

    @Inject
    public GatewayWebSocketListener(
        ScheduledExecutorService scheduler,
        ObjectMapper mapper,
        @ConfigProperty(name = "trust.discord.token") String token
    ) {
        Objects.requireNonNull(scheduler);
        Objects.requireNonNull(mapper);

        this.scheduler = scheduler;
        this.mapper = mapper;
        this.token = token;

    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        Objects.requireNonNull(webSocket);
        Objects.requireNonNull(data);

        LOGGER.info("Received text: {}", data);

        JsonNode node = null;
        try {
            node = this.mapper.readTree(data.toString());
        } catch (JsonProcessingException ex) {
            LOGGER.error("Failed to deserialize data: {}", ex);
            return CompletableFuture.failedFuture(ex);
        }

        if (node.has(S)) {
            int s = node.get(S).asInt();
            this.sequence.getAndSet(s);
        }

        int opCode = node.get(OP).asInt();
        CompletionStage<?> ret = null;
        switch (opCode) {
        case 1:
            LOGGER.info("Heartbeat requested.");
            this.handleOpCode1(webSocket);
            break;
        case 10:
            this.handleOpCode10(webSocket, node);
            break;
        case 11:
            this.handleOpCode11();
            break;
        default:
            break;
        }

        // if (ret != null) {
        //     ret.whenComplete((v, ex) -> {
        //         if (ex != null) {
        //             LOGGER.error("Error from opcode handler.", ex);
        //         }
        //     });
        // }

        return ret;
    }

    /**
     * Handles the requested heartbeat op code. As described in the docs this is a
     * two way opcode and should immediately send the heartbeat opcode.
     */
    private void handleOpCode1(WebSocket webSocket) {
        try {
            String response = this.mapper.writeValueAsString(new GatewayResponse<>(1, this.sequence.get()));

            // Shut-up error prone
            @SuppressWarnings("unused")
            CompletionStage<?> f = webSocket.sendText(response, true);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Failed to serialize GatewayResponse", ex);
        }
    }

    /**
     * Handles the initial hello opcode from the gateway. As described by the docs
     * this should send the heartbeat and then send opcode 2 identity to the
     * gateway.
     */
    @SuppressWarnings("FutureReturnValueIgnored")
    private void handleOpCode10(WebSocket webSocket, JsonNode data) {
        int interval = data.get("heartbeat_interval").asInt();

        // todo cancel on resum
        this.heartbeat.set(this.scheduler.scheduleAtFixedRate(
            () -> {
                LOGGER.info("Sending heartbeat.");
                if (this.ack.compareAndSet(true, false)) {
                    this.handleOpCode1(webSocket);
                } else {
                    webSocket.sendClose(4000, "").thenApply(this::resume);
                }
            },
            interval,
            interval,
            TimeUnit.MILLISECONDS
        ));

        try {
            var m = Map.of(
                "token", this.token,
                "properties", Map.of("$os", "linux", "$browser", SFXD_TRUST_BOT, "$device", SFXD_TRUST_BOT)
            );

            webSocket.sendText(this.mapper.writeValueAsString(m), true);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Failed to serialize identity.", ex);
        }
    }

    private void handleOpCode11() {
        this.ack.set(true);
    }

    private CompletableFuture<WebSocket> resume(WebSocket webSocket) {
        try {
            var m = Map.of(
                "token", this.token,
                "session_id", "",
                "seq", this.sequence.get()
            );

            return webSocket.sendText(this.mapper.writeValueAsString(m), true);
        } catch (JsonProcessingException ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }
}
