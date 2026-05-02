package com.github.sfxd.trust.instances;

import com.github.sfxd.trust.Messages;
import com.github.sfxd.trust.events.Event;
import com.github.sfxd.trust.events.EventHandler;
import com.github.sfxd.trust.util.SimpleMessage;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

public record InstanceUpdatedEvent(Instance instance) implements Event {

    @Singleton
    public static class Handler implements EventHandler<InstanceUpdatedEvent> {
        private static final String MESSAGE = """
                Instance %s has been updated:
                  status: %s
                  location: %s
                  release version: %s
                  release number: %s
                  environment: %s
            """;
        private final Messages messages;

        @Inject
        public Handler(Messages messages) {
            this.messages = messages;
        }

        @Override
        public Class<InstanceUpdatedEvent> type() {
            return InstanceUpdatedEvent.class;
        }

        @Override
        public void accept(InstanceUpdatedEvent event) {
            var instance = event.instance();
            var message = MESSAGE.formatted(
                instance.key(),
                instance.status(),
                instance.location(),
                instance.releaseVersion(),
                instance.releaseNumber(),
                instance.environment()
            );

            for (var subscription : instance.subscriptions()) {
                this.messages.send(new SimpleMessage(subscription.user(), message));
            }
        }
    }
}
