package com.github.sfxd.trust.events;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PipelineTests {

    @Test
    void sendsForEachHandler() {
        var handlerOne = new Handler();
        var handlerTwo = new Handler();

        var pipeline = new Pipeline.Builder()
            .addMapping(handlerOne)
            .addMapping(handlerTwo)
            .build();

        pipeline.send(List.of(new HandlerEvent()));

        assertTrue(handlerOne.handled);
        assertTrue(handlerTwo.handled);
    }

    private static class HandlerEvent implements Event {}
    private static class Handler implements EventHandler<HandlerEvent> {
        private boolean handled;

        @Override
        public Class<HandlerEvent> type() {
            return HandlerEvent.class;
        }

        @Override
        public void accept(HandlerEvent handlerEvent) {
            this.handled = true;
        }
    }
}
