package com.github.sfxd.trust.producers;

import java.net.http.HttpClient;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

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
}
