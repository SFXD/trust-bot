package com.github.sfxd.trust.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.sfxd.trust.runtime.RuntimeManager.StartupHook;

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
public class TaskManager implements StartupHook {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);

    private final List<Task> tasks = new ArrayList<>();
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

        for (Task t : tasks) {
            this.tasks.add(t);
        }
    }

    @SuppressWarnings("FutureReturnValueIgnored")
    @Override
    public void onStart() {
        LOGGER.info("Scheduling {} tasks", this.tasks.size());

        for (Task task : this.tasks) {
            this.scheduler.scheduleAtFixedRate(
                () -> {
                    CompletableFuture.runAsync(task, this.worker).whenComplete((ignored, ex) -> {
                        if (ex != null) {
                            LOGGER.error("Uncaught exception from scheduled task", ex);
                        }
                    });
                },
                0L,
                task.interval(),
                task.timeUnit()
            );
        }
    }

    public interface Task extends Runnable {
        long interval();
        TimeUnit timeUnit();
    }
}
