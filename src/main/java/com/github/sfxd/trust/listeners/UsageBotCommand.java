package com.github.sfxd.trust.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

class UsageBotCommand extends BotCommand {

    static final String USAGE = "Usage: \n" +
                                "  !trust <subscribe, unsubscribe> <instance_id>\n" +
                                "  !trust source\n";

    UsageBotCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void run() {
        this.event.getChannel().sendMessage(USAGE).queue();
    }
}
