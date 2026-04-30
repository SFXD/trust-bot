package com.github.sfxd.trust.instances;

import com.github.sfxd.trust.Messages;
import com.github.sfxd.trust.events.Event;
import com.github.sfxd.trust.events.EventHandler;
import com.github.sfxd.trust.users.Subscription;
import com.github.sfxd.trust.util.SimpleMessage;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

public record InstanceStatusChangeEvent(Instance instance) implements Event {
    @Singleton
    public static class Handler implements EventHandler<InstanceStatusChangeEvent> {
        private final Messages messages;

        @Inject
        public Handler(Messages messages) {
            this.messages = messages;
        }

        @Override
        public Class<InstanceStatusChangeEvent> type() {
            return InstanceStatusChangeEvent.class;
        }

        @Override
        public void accept(InstanceStatusChangeEvent event) {
            var body = "Instance %s status updated: %s".formatted(event.instance().key(), event.instance().status());
            for (Subscription subscription : event.instance().subscriptions()) {
                this.messages.send(new SimpleMessage(subscription.user(), body));
            }
        }
    }
}
