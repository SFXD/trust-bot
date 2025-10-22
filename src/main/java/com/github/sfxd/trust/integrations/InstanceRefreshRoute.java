// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.github.sfxd.trust.util.Json;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;

/**
 * Task that will check the Trust api instances and update their data in the
 * database.
 */
@SuppressWarnings("unused") // reflectively found by DI
@Singleton
public class InstanceRefreshRoute extends RouteBuilder {
    private final InstanceRefreshConsumer consumer;

    @Inject
    public InstanceRefreshRoute(InstanceRefreshConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    @SuppressWarnings("unchecked") // generic type erasure
    public void configure() throws Exception {
        from("timer:instance-refresh?period=60000&fixedRate=true")
            .to("https://api.status.salesforce.com/v1/instances/status/preview?httpMethod=GET&throwExceptionOnFailure=false")
            .choice()
                .when(header("CamelHttpResponseCode").isEqualTo(200))
                    .log(LoggingLevel.INFO, "Running instance refresh route")
                    .unmarshal(new JsonDataFormat<>(InstancePreviewViewModel.class))
                    .process().body(Collection.class, this.consumer::accept)
                .endChoice()
                .otherwise()
                    .log(LoggingLevel.WARN, "Trust api not-ok: ${headers.CamelHttpResponseCode}")
                .endChoice()
            .end();
    }

    private static class JsonDataFormat<T> implements DataFormat {
        private final Class<T> clazz;

        private JsonDataFormat(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
            return Json.deserialize(stream, this.clazz);
        }

        @Override
        public void start() {
            // No idea what this does. Camel is a web of enterprise bullshit.
        }

        @Override
        public void stop() {
            // No idea what this does. Camel is a web of enterprise bullshit.
        }
    }
}
