// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations.cdi;


import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.List;

@Factory
public class CamelProducer {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public CamelContext produceCamelContext(List<RouteBuilder> builders) throws Exception {
        @SuppressWarnings("resource") // shutdown when the cdi container shuts down
        var context = new DefaultCamelContext();

        for (var builder : builders) {
            context.addRoutes(builder);
        }

        return context;
    }
}
