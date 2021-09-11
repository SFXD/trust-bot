// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.integrations.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.jboss.weld.environment.se.events.ContainerBeforeShutdown;
import org.jboss.weld.environment.se.events.ContainerInitialized;

class CamelProducer {

    @ApplicationScoped
    @Produces
    CamelContext produceCamelContext(@Any Instance<RouteBuilder> builders) throws Exception {
        @SuppressWarnings("resource") // shutdown when the cdi container shuts down
        var context = new DefaultCamelContext();

        for (var builder : builders) {
            context.addRoutes(builder);
        }

        return context;
    }

    void onStart(@Observes ContainerInitialized initialized, CamelContext context) {
        context.start();
    }

    void onStop(@Observes ContainerBeforeShutdown beforeShutDown, CamelContext context) {
        context.shutdown();
    }
}
