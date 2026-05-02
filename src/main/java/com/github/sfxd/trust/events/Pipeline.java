package com.github.sfxd.trust.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline {
    private final Map<Class<?>, List<EventHandler<Event>>> mappings = new HashMap<>();

    private Pipeline() {}

    public void send(Event event) {
        List<EventHandler<Event>> handlers = this.mappings.get(event.getClass());
        if (handlers == null) {
            throw new IllegalStateException("no event handler found");
        }

        for (var handler : handlers) {
            handler.accept(event);
        }
    }

    public static class Builder {
        private final Pipeline pipeline = new Pipeline();

        @SuppressWarnings("unchecked")
        public <T extends Event> Builder addMapping(EventHandler<T> handler) {
            this.pipeline.mappings.computeIfAbsent(handler.type(), _ -> new ArrayList<>())
                .add((EventHandler<Event>) handler);
            return this;
        }

        public Pipeline build() {
            return this.pipeline;
        }
    }
}
