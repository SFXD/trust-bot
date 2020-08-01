package com.github.sfxd.trust.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

abstract class AbstractBotCommand implements BotCommand {
    static final String CHECK_MARK = "✅";

    protected final MessageReceivedEvent event;

    protected AbstractBotCommand(MessageReceivedEvent event) {
        this.event = event;
    }

    protected void reactWithCheckMark() {
        this.event.getMessage().addReaction(CHECK_MARK).queue();
    }
}
