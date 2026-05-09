package com.github.sfxd.trust.events;

import io.ebean.event.BeanPersistListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class EventfulBeanPersistListener implements BeanPersistListener {
    private final Pipeline pipeline;

    @Inject
    public EventfulBeanPersistListener(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public boolean isRegisterFor(Class<?> cls) {
        return Eventful.class.isAssignableFrom(cls);
    }

    @Override
    public void inserted(Object bean) {
        this.send(bean);
    }

    @Override
    public void updated(Object bean, Set<String> updatedProperties) {
        this.send(bean);
    }

    @Override
    public void deleted(Object bean) {
        this.send(bean);
    }

    @Override
    public void softDeleted(Object bean) {
        this.send(bean);
    }

    private void send(Object obj) {
        var events = ((Eventful) obj).events();
        this.pipeline.send(events);
    }
}
