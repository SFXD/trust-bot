package com.github.sfxd.trust.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.github.sfxd.trust.runtime.RuntimeManager.StartupHook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The task manager manages the running of scheduled jobs.
 */
@ApplicationScoped
public class TaskManager implements StartupHook {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);

    private final List<Task> tasks = new ArrayList<>();
    private final ScheduledExecutorService scheduler;

    @Inject
    public TaskManager(@Any Instance<Task> tasks, ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;

        for (Task t : tasks) {
            this.tasks.add(t);
        }
    }

    @SuppressWarnings("FutureReturnValueIgnored")
    @Override
    public void onStart() {
        LOGGER.info("Scheduling {} tasks", this.tasks.size());

        for (Task task : this.tasks) {
            this.scheduler.scheduleAtFixedRate(task, 0L, task.interval(), task.timeUnit());
        }
    }

    public interface Task extends Runnable {
        long interval();
        TimeUnit timeUnit();
    }
}
