package com.github.sfxd.trust.listeners;

import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

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
@Singleton
public class MessageListener extends ListenerAdapter {
    static final String CHECK_MARK = "✅";
    static final String USAGE = "Usage: \n" +
                                "  !trust <subscribe, unsubscribe> <instance_id>\n" +
                                "  !trust source\n";
    static final String GITHUB = "https://github.com/SFXD/trust-bot";

    private final SubscriberService subscriberService;
    private final InstanceService instanceService;
    private final InstanceSubscriberService instanceSubscriberService;

    @Inject
    public MessageListener(
        SubscriberService subscriberService,
        InstanceService instanceService,
        InstanceSubscriberService instanceSubscriberService
    ) {
        Objects.requireNonNull(subscriberService);
        Objects.requireNonNull(instanceService);
        Objects.requireNonNull(instanceSubscriberService);

        this.subscriberService = subscriberService;
        this.instanceService = instanceService;
        this.instanceSubscriberService = instanceSubscriberService;
    }

    /**
     * Handles the message received event. If the message doesn't start with !trust
     * it will do nothing, if it does if will check if the next word is one of our
     * supported commands (subscribe or unsubscribe). If the command doesn't have an
     * instance provided it will respond in the channel with how to use the command.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ", -1);
        if (!split[0].equalsIgnoreCase("!trust")) {
            return;
        }

        if (split.length < 2) {
            this.printUsage(event);
            return;
        }

        String command = split[1].toLowerCase();
        String key = null;
        switch (command) {
        case "subscribe":
            key = split[2].toUpperCase();
            this.handleSubscribe(event, key);
            return;
        case "unsubscribe":
            key = split[2].toUpperCase();
            this.handleUnsubscribe(event, key);
            return;
        case "source":
            this.handleSource(event);
            return;
        default:
            this.printUsage(event);
            return;
        }
    }

    /**
     * Handles the subscribe command. This will sign a user up for notifications
     * for the given key.
     * @param event The event from JDA
     * @param key the instance key from the command.
     */
    private void handleSubscribe(MessageReceivedEvent event, String key) {
        Optional<Instance> instance = this.instanceService.findByKey(key);

        if (!instance.isPresent()) {
            event.getChannel().sendMessage(String.format("%s is not a valid instance key.", key)).queue();
            return;
        }

        String username = event.getAuthor().getName();
        Subscriber subscriber = this.subscriberService.findByUsername(username)
            .orElseGet(() -> new Subscriber(username));

        if (subscriber.isNew()) {
            this.subscriberService.insert(subscriber);
        }

        Optional<InstanceSubscriber> subscription = this.instanceSubscriberService.findByInstanceIdAndSubscriberId(
            instance.get().getId(),
            subscriber.getId()
        );

        if (!subscription.isPresent()) {
            this.instanceSubscriberService.insert(new InstanceSubscriber(instance.get(), subscriber));
        }

        event.getChannel().addReactionById(event.getMessageId(), CHECK_MARK).queue();
    }

    /**
     * Handles the unsubscribe command. This will delete the subscription if it
     * finds it.
     *
     * @param event The event from JDA.
     * @param key   The instance key from the command.
     */
    private void handleUnsubscribe(MessageReceivedEvent event, String key) {
        Optional<InstanceSubscriber> subscription = this.instanceSubscriberService.findByKeyAndUsername(
            key,
            event.getAuthor().getName()
        );

        if (subscription.isPresent()) {
            this.instanceSubscriberService.delete(subscription.get());
        }

        event.getChannel().addReactionById(event.getMessageId(), CHECK_MARK).queue();
    }

    /**
     * Handles the source command. This should respond with a link to the source
     * code of this project.
     *
     * @param event the event from jda
     */
    private void handleSource(MessageReceivedEvent event) {
        event.getChannel().sendMessage(GITHUB).queue();
    }

    private void printUsage(MessageReceivedEvent event) {
        event.getChannel()
            .sendMessage(
                "Usage: \n" +
                "  !trust <subscribe, unsubscribe> <instance_id>\n" +
                "  !trust source\n"
            )
            .queue();
    }
}
