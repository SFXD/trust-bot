package com.github.sfxd.trust.runtime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.JDA;

class JDACdiLifeCycleTests {

    @Test
    void it_should_await_ready_on_start() throws Exception {
        var jda = mock(JDA.class);
        var lifecycle = new JDACdiLifeCycle(jda);

        lifecycle.onStart(null);

        verify(jda).awaitReady();
    }

    @Test
    void it_should_shutdown_on_stop() {
        var jda = mock(JDA.class);
        var lifecycle = new JDACdiLifeCycle(jda);

        lifecycle.onStop(null);

        verify(jda).shutdown();
    }
}
