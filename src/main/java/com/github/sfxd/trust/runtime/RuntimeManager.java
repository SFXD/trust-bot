// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

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

package com.github.sfxd.trust.runtime;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The runtime manager handles anything that needs to happen when the application
 * starts or when the application shutsdown.
 */
@ApplicationScoped
public class RuntimeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeManager.class);

    private final List<ShutdownHook> shutdownHooks = new ArrayList<>();
    private final List<StartupHook> startupHooks = new ArrayList<>();

    @Inject
    public RuntimeManager(@Any Instance<StartupHook> startupHooks, @Any Instance<ShutdownHook> shutdownHooks) {
        for (StartupHook hook : startupHooks) {
            this.startupHooks.add(hook);
        }

        for (ShutdownHook hook : shutdownHooks) {
            this.shutdownHooks.add(hook);
        }
    }

    @PostConstruct
    void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onStop));
    }

    public void onStop() {
        LOGGER.info("Running {} shutdown hooks", this.shutdownHooks.size());

        for (ShutdownHook hook : this.shutdownHooks) {
            try {
                hook.onStop();
            } catch (Exception ex) {
                LOGGER.error("Uncaught exception during shutdown", ex);
            }
        }
    }

    public void onStart() {
        LOGGER.info("Running {} startup hooks", this.startupHooks.size());

        for (StartupHook hook : this.startupHooks) {
            hook.onStart();
        }
    }

    @FunctionalInterface
    public interface ShutdownHook {
        void onStop();
    }

    @FunctionalInterface
    public interface StartupHook {
        void onStart();
    }
}
