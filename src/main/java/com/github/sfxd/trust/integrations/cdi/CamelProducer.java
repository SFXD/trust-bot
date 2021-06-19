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
