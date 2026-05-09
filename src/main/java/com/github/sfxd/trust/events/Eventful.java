package com.github.sfxd.trust.events;

import java.util.Collection;

public interface Eventful {
    Collection<Event> events();
    void addEvent(Event event);
}
