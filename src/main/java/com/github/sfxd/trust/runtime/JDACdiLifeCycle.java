package com.github.sfxd.trust.runtime;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.environment.se.events.ContainerBeforeShutdown;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;

/**
 * CDI life cycle hooks for JDA
 */
@ApplicationScoped
public class JDACdiLifeCycle  {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDACdiLifeCycle.class);

    private final JDA jda;

    @Inject
    public JDACdiLifeCycle(JDA jda) {
        Objects.requireNonNull(jda);

        this.jda = jda;
    }

    public void onStart(@Observes ContainerInitialized initialized) throws InterruptedException, IllegalStateException {
        LOGGER.info("Awaiting JDA ready");
        this.jda.awaitReady();
    }

    public void onStop(@Observes ContainerBeforeShutdown beforeShutdown) {
        LOGGER.info("Shutting down JDA");
        this.jda.shutdown();
    }
}
