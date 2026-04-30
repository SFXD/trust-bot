package com.github.sfxd.trust.events;

public interface EventHandler<T extends Event> {
    Class<T> type();
    void accept(T t);
}
