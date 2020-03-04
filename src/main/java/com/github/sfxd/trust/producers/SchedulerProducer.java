package com.github.sfxd.trust.producers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Producers for scheduling actions.
 */
class SchedulerProducer {

    /**
     * Produces a single threaded executor for scheduling actions.
     * @return {@link Executors#newSingleThreadExecutor()}
     */
    @Produces
    @ApplicationScoped
    ScheduledExecutorService produceScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
