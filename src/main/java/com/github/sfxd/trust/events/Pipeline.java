package com.github.sfxd.trust.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executor;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNullElse;

public class Pipeline {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pipeline.class);
    private final Map<Class<?>, List<EventHandler<Event>>> mappings;
    private final Executor executor;

    private Pipeline(Map<Class<?>, List<EventHandler<Event>>> mappings, Executor executor) {
        this.mappings = mappings;
        this.executor = executor;
    }

    public void send(Collection<Event> events) {
        // Off-load to a background executor so it doesn't block Ebean's background threads.
        this.executor.execute(() -> events.forEach(this::send));
    }

    private void send(Event event) {
        var handlers = this.mappings.get(event.getClass());
        if (handlers == null) {
            LOGGER.warn("No handler found for {}", event.getClass());
            return;
        }

        handlers.forEach(handler -> handle(handler, event));
    }

    private static void handle(EventHandler<Event> handler, Event event) {
        try {
            handler.accept(event);
        } catch (Exception ex) {
            LOGGER.error("Uncaught EventHandler error", ex);
        }
    }

    public static class Builder {
        private final Map<Class<?>, List<EventHandler<Event>>> mappings = new HashMap<>();
        private Executor executor;

        @SuppressWarnings("unchecked")
        public <T extends Event> Builder addMapping(EventHandler<T> handler) {
            this.mappings.computeIfAbsent(handler.type(), _ -> new ArrayList<>())
                .add((EventHandler<Event>) handler);
            return this;
        }

        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Pipeline build() {
            return new Pipeline(unmodifiableMap(mappings), requireNonNullElse(this.executor, Runnable::run));
        }
    }
}
