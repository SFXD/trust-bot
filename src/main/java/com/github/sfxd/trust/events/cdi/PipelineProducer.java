package com.github.sfxd.trust.events.cdi;

import com.github.sfxd.trust.events.EventHandler;
import com.github.sfxd.trust.events.Pipeline;
import io.avaje.inject.Bean;

import java.util.List;

public class PipelineProducer {

    @Bean
    public Pipeline producePipeline(List<EventHandler<?>> handlers) {
        var builder = new Pipeline.Builder();
        for (var handler : handlers) {
            builder.addMapping(handler);
        }

        return builder.build();
    }
}
