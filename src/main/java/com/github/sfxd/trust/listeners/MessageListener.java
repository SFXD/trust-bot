package com.github.sfxd.trust.listeners;

import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.services.InstanceService;
import com.github.sfxd.trust.services.InstanceSubscriberService;
import com.github.sfxd.trust.services.SubscriberService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listener that watches messages that start with !trust
 */
@ApplicationScoped
public class MessageListener extends ListenerAdapter {

    private final SubscriberService subscriberService;
    private final InstanceService instanceService;
    private final InstanceSubscriberService instanceSubscriberService;

    @Inject
    public MessageListener(
        SubscriberService subcriberService,
        InstanceService instanceService,
        InstanceSubscriberService instanceSubscriberService
    ) {
        Objects.requireNonNull(subcriberService);
        Objects.requireNonNull(instanceService);
        Objects.requireNonNull(instanceSubscriberService);

        this.subscriberService = subcriberService;
        this.instanceService = instanceService;
        this.instanceSubscriberService = instanceSubscriberService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        if (!split[0].equals("!trust")) {
            return;
        }

        if (split.length < 3) {
            event.getChannel()
                .sendMessage("Usage: !trust <subscribe, unsubscribe> <instance_id>")
                .queue();

            return;
        }

        String command = split[1].toLowerCase();
        String key = split[2].toUpperCase();
        switch (command) {
        case "subscribe":
            this.handleSubscribe(event, key);
            return;
        case "unsubscribe":
            this.handleUnsubscribe(event, key);
            return;
        default:
            return;
        }
    }

    private void handleSubscribe(MessageReceivedEvent event, String key) {
        Optional<Instance> instance = this.instanceService.findByKey(key);

        String username = event.getAuthor().getAsTag();
        Subscriber subscriber = this.subscriberService.findByUsername(username)
                .orElseGet(() -> new Subscriber(username));

        if (instance.isPresent()) {
            this.instanceSubscriberService.insert(new InstanceSubscriber(instance.get(), subscriber));
            event.getChannel().addReactionById(event.getMessageId(), "✅").queue();
        } else {
            event.getChannel().sendMessage(String.format("Could not find instance with id: %s", key));
        }
    }

    private void handleUnsubscribe(MessageReceivedEvent event, String key) {

    }
}
