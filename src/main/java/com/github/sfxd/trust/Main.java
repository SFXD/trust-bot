package com.github.sfxd.trust;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import com.github.sfxd.trust.listeners.MessageListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {
    public static void main(String[] args) throws Exception {
        var parser = ArgumentParsers.newFor("trust-bot")
            .build()
            .defaultHelp(true);

        parser.addArgument("-t", "--token")
            .help("discord token")
            .required(true);

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException ex) {
            parser.handleError(ex);
            System.exit(1);
        }

        var initializer = SeContainerInitializer.newInstance();
        try (SeContainer container = initializer.initialize()) {
            var listener = container.select(MessageListener.class);

            @SuppressWarnings("unused")
            JDA jda = new JDABuilder(ns.getString("token"))
                .setActivity(Activity.playing("In Vip we trust."))
                .addEventListeners(listener)
                .build();
        }
    }
}
