package com.github.sfxd.trust.inject;

import com.github.sfxd.trust.events.EventHandler;
import com.github.sfxd.trust.events.Pipeline;
import io.avaje.inject.Bean;
import io.avaje.inject.Factory;

import java.util.List;
import java.util.concurrent.Executors;

@Factory
public class PipelineProducer {

    @Bean
    public Pipeline producePipeline(List<EventHandler<?>> handlers) {
        var builder = new Pipeline.Builder()
            .executor(Executors.newVirtualThreadPerTaskExecutor());
        for (var handler : handlers) {
            var _ = builder.addMapping(handler);
        }

        return builder.build();
    }
}
