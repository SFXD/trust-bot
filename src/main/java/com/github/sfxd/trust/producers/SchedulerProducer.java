package com.github.sfxd.trust.producers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

class SchedulerProducer {

    @Produces
    @ApplicationScoped
    ScheduledExecutorService produceScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Produces
    @Named("scheduler-worker")
    @ApplicationScoped
    ExecutorService produceWorker() {
        return Executors.newSingleThreadExecutor();
    }
}
