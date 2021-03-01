package com.github.sfxd.trust.listeners;

import java.util.Optional;

import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/** Command for !trust unsubscribe <serverkey> */
class UnsubscribeBotCommand extends BotCommand {
    private final String key;
    private final InstanceSubscriberFinder isFinder;
    private final InstanceSubscriberService isService;

    UnsubscribeBotCommand(
        MessageReceivedEvent event,
        String key,
        InstanceSubscriberFinder isFinder,
        InstanceSubscriberService isService
    ) {
        super(event);

        this.key = key;
        this.isFinder = isFinder;
        this.isService = isService;
    }

    @Override
    public void run() {
        Optional<InstanceSubscriber> subscription = this.isFinder
            .findByKeyAndUsername(this.key, this.event.getAuthor().getId());

        if (subscription.isPresent()) {
            try {
                this.isService.delete(subscription.get());
            } catch (DmlException ex) {
                throw new BotCommandException(ex);
            }
        }

        this.reactWithCheckMark();
    }
}
