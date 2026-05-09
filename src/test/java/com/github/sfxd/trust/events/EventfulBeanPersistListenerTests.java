package com.github.sfxd.trust.events;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventfulBeanPersistListenerTests {

    @Test
    void isOnlyRegisteredForEventfulTypes() {
        var pipeline = new Pipeline.Builder()
            .addMapping(new TestEventfulHandler())
            .build();
        var listener = new EventfulBeanPersistListener(pipeline);
        assertTrue(listener.isRegisterFor(TestEventful.class));
        assertFalse(listener.isRegisterFor(Integer.class));
    }

    private static class TestEventful implements Eventful {
        @Override
        public Collection<Event> events() {
            return List.of(new TestEvent());
        }

        @Override
        public void addEvent(Event event) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void isSentForInserted() {
        doSentTest(listener -> listener.inserted(new TestEventful()));
    }

    @Test
    void isSentForUpdated() {
        doSentTest(listener -> listener.updated(new TestEventful(), Set.of()));
    }

    @Test
    void isSentForDeleted() {
        doSentTest(listener -> listener.deleted(new TestEventful()));
    }

    @Test
    void isSentForSoftDelete() {
        doSentTest(listener -> listener.softDeleted(new TestEventful()));
    }

    private void doSentTest(Consumer<EventfulBeanPersistListener> fn) {
        var handler = new TestEventfulHandler();
        var pipeline = new Pipeline.Builder()
            .addMapping(handler)
            .build();
        var listener = new EventfulBeanPersistListener(pipeline);
        fn.accept(listener);

        assertTrue(handler.sent);
    }

    private static class TestEvent implements Event {}
    private static class TestEventfulHandler implements EventHandler<TestEvent> {
        private boolean sent;

        @Override
        public Class<TestEvent> type() {
            return TestEvent.class;
        }

        @Override
        public void accept(TestEvent testEvent) {
            this.sent = true;
        }
    }
}
