package com.github.sfxd.trust.producers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

class SchedulerProducer {

    @Produces
    @ApplicationScoped
    ScheduledExecutorService produceScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
