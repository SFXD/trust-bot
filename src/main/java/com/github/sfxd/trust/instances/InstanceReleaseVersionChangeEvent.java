package com.github.sfxd.trust.instances;

import com.github.sfxd.trust.Messages;
import com.github.sfxd.trust.events.Event;
import com.github.sfxd.trust.events.EventHandler;
import com.github.sfxd.trust.util.SimpleMessage;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

public record InstanceReleaseVersionChangeEvent(Instance instance) implements Event {
    @Singleton
    public static class Handler implements EventHandler<InstanceReleaseVersionChangeEvent> {
        private final Messages messages;

        @Inject
        public Handler(Messages messages) {
            this.messages = messages;
        }

        @Override
        public Class<InstanceReleaseVersionChangeEvent> type() {
            return InstanceReleaseVersionChangeEvent.class;
        }

        @Override
        public void accept(InstanceReleaseVersionChangeEvent event) {
            var instance = event.instance();
            var body = "Instance %s release version updated: %s".formatted(instance.key(), instance.releaseVersion());
            for (var subscription : instance.subscriptions()) {
                this.messages.send(new SimpleMessage(subscription.user(), body));
            }
        }
    }
}
