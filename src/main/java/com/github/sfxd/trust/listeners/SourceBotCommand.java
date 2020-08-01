package com.github.sfxd.trust.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class SourceBotCommand extends AbstractBotCommand {
    static final String GITHUB = "https://github.com/SFXD/trust-bot";

    SourceBotCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void run() {
        this.event.getChannel().sendMessage(GITHUB).queue();
    }
}
