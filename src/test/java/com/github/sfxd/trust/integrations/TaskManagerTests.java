package com.github.sfxd.trust.integrations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Instance;

import com.github.sfxd.trust.integrations.TaskManager.Task;

import org.junit.jupiter.api.Test;

class TaskManagerTests {

    @Test
    void it_should_shutdown_the_schedulers_on_stop() {
        var scheduler = mock(ScheduledExecutorService.class);
        var worker = mock(ExecutorService.class);

        @SuppressWarnings("unchecked")
        var tasks = (Instance<Task>) mock(Instance.class);

        var mgr = new TaskManager(tasks, scheduler, worker);
        mgr.onStop(null);

        verify(scheduler).shutdown();
        verify(worker).shutdown();
    }

    @Test
    void it_should_schedule_all_tasks_on_start() {
        var scheduler = mock(ScheduledExecutorService.class);
        var worker = mock(ExecutorService.class);

        @SuppressWarnings("unchecked")
        var tasks = (Instance<Task>) mock(Instance.class);
        var iter = List.of(
            new Task() {

                @Override
                public void run() {

                }

                @Override
                public TimeUnit timeUnit() {
                    return null;
                }

                @Override
                public long interval() {
                    return 0;
                }
            },
            new Task() {

                @Override
                public void run() {

                }

                @Override
                public TimeUnit timeUnit() {
                    return null;
                }

                @Override
                public long interval() {
                    return 0;
                }
            }
        );

        when(tasks.stream()).thenReturn(iter.stream());
        var mgr = new TaskManager(tasks, scheduler, worker);
        mgr.onStart(null);

        verify(scheduler, times(iter.size())).scheduleAtFixedRate(any(), anyLong(), anyLong(), any());
    }
}
