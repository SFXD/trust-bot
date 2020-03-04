package com.github.sfxd.trust.producers;

import com.github.sfxd.trust.GatewayWebSocketListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Producers for all things HTTP.
 */
class HttpProducer {

    /**
     * Produces the http client for the application.
     * @return standard http client from {@link HttpClient#newHttpClient()}
     */
    @Produces
    @ApplicationScoped
    HttpClient produceHttpClient() {
        return HttpClient.newHttpClient();
    }

    /**
     * Produces the websocket that will be used to communicate with the
     * discord real time gateway.
     *
     * @throws java.util.concurrent.CancellationException
     * @throws java.util.concurrent.CompletionException
     */
    @Produces
    @ApplicationScoped
    WebSocket produceWebSocket(
        URI uri,
        HttpClient client,
        GatewayWebSocketListener listener
    ) {
        var builder = client.newWebSocketBuilder();
        return builder.buildAsync(uri, listener).join();
    }

    /** URI to the discord gateway */
    @Produces
    @ApplicationScoped
    URI produceGatewayUri(@ConfigProperty(name = "trust.discordw.websocket.url") String url) {
        Objects.requireNonNull(url);

        return URI.create(url);
    }
}
