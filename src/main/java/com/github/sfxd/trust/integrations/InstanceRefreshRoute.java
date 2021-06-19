// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2021 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
                    .unmarshal(new ListJacksonDataFormat(Json.MAPPER, Instance.class))
                    .process().body(Collection.class, this.consumer::accept)
                .endChoice()
                .otherwise()
                    .log(LoggingLevel.WARN, "Trust api not-ok: ${headers.CamelHttpResponseCode}")
                .endChoice()
            .end();
    }
}
