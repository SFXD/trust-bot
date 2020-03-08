package com.github.sfxd.trust.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A junction between instances and their subscribers.
 */
@Entity
public class InstanceSubscriber extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instanceId", nullable = false)
    private Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscriberId", nullable = false)
    private Subscriber subscriber;

    private boolean isActive = true;

    public InstanceSubscriber() {

    }

    public Instance getInstance() {
        return this.instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Subscriber getSubscriber() {
        return this.subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
