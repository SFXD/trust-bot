// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.github.sfxd.trust.tasks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.environment.se.events.ContainerBeforeShutdown;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The task manager manages the running of scheduled jobs.
 *
 * The task manager is designed to take two executors:
 *  1. A scheduled executor that handles invoking the jobs.
 *  2. A worker executor that is designed to actually run the job.
 *
 * The second worker is required so that the scheduler can always
 * be available to schedule actions on their interval.
 *
 * Sadly, the other reason is a failure in the jdk api for executors.
 * It is not possible to handle errors from the Future interface
 * and creates a cumbersome api where scheduled actions could fail
 * silently.
 */
@ApplicationScoped
public class TaskManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);

    private final List<Task> tasks;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService worker;

    @Inject
    public TaskManager(
        @Any Instance<Task> tasks,
        ScheduledExecutorService scheduler,
        @Named("scheduler-worker") ExecutorService worker
    ) {
        this.scheduler = scheduler;
        this.worker = worker;

        this.tasks = tasks.stream().collect(Collectors.toList());
    }

    @SuppressWarnings("FutureReturnValueIgnored")
    public void onStart(@Observes ContainerInitialized containerInitialized) {
        LOGGER.info("Scheduling {} tasks", this.tasks.size());

        for (Task task : this.tasks) {
            this.scheduler.scheduleAtFixedRate(
                () -> CompletableFuture.runAsync(task, this.worker).whenComplete(TaskManager::logIfError),
                0L,
                task.interval(),
                task.timeUnit()
            );
        }
    }

    private static <T> void logIfError(T ignored, Throwable ex) {
        if (ex != null) {
            LOGGER.error("Uncaught exception from scheduled task", ex);
        }
    }

    public void onStop(@Observes ContainerBeforeShutdown shutdown) {
        LOGGER.info("Shutting down task scheduler");
        this.scheduler.shutdown();
        this.worker.shutdown();
    }

    public interface Task extends Runnable {
        long interval();
        TimeUnit timeUnit();
    }
}
