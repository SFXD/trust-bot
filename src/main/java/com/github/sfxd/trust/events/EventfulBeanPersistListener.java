package com.github.sfxd.trust.events;

import io.ebean.event.BeanPersistListener;

import java.util.Set;

public class EventfulBeanPersistListener implements BeanPersistListener {
    private final Pipeline pipeline;

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
        ((Eventful) obj).events().forEach(this.pipeline::send);
    }
}
