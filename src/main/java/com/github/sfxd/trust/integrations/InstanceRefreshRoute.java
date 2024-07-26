// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations;

import java.util.Collection;

import javax.inject.Inject;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.util.Json;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;

/**
 * Task that will check the Trust api instances and update their data in the
 * database.
 */
@SuppressWarnings("unused") // reflectively found by DI
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
                    .unmarshal(new ListJacksonDataFormat(Json.mapper(), Instance.class))
                    .process().body(Collection.class, this.consumer::accept)
                .endChoice()
                .otherwise()
                    .log(LoggingLevel.WARN, "Trust api not-ok: ${headers.CamelHttpResponseCode}")
                .endChoice()
            .end();
    }
}
