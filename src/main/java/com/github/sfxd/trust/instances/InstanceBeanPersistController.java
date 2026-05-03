package com.github.sfxd.trust.instances;

import io.ebean.event.BeanDeleteIdRequest;
import io.ebean.event.BeanPersistController;
import io.ebean.event.BeanPersistRequest;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class InstanceBeanPersistController implements BeanPersistController {
    private static final Set<String> PROPS_FOR_UPDATE_EVENT = Set.of(
        "status",
        "environment",
        "releaseVersion",
        "location",
        "releaseNumber"
    );

    @Override
    public boolean isRegisterFor(Class<?> clz) {
        return Instance.class.equals(clz);
    }

    @Override
    public void postUpdate(BeanPersistRequest<?> request) {
        if (request.hasDirtyProperty(PROPS_FOR_UPDATE_EVENT)) {
            var instance = (Instance) request.bean();
            instance.addEvent(new InstanceUpdatedEvent(instance));
        }
    }

    @Override
    public int getExecutionOrder() {
        return 0;
    }

    @Override
    public boolean preInsert(BeanPersistRequest<?> request) {
        return true;
    }

    @Override
    public boolean preUpdate(BeanPersistRequest<?> request) {
        return true;
    }

    @Override
    public boolean preDelete(BeanPersistRequest<?> request) {
        return true;
    }

    @Override
    public boolean preSoftDelete(BeanPersistRequest<?> request) {
        return true;
    }

    @Override
    public void preDelete(BeanDeleteIdRequest request) {

    }

    @Override
    public void postInsert(BeanPersistRequest<?> request) {

    }

    @Override
    public void postDelete(BeanPersistRequest<?> request) {

    }

    @Override
    public void postSoftDelete(BeanPersistRequest<?> request) {

    }
}
