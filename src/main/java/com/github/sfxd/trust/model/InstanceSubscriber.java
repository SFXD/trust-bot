package com.github.sfxd.trust.model;

import java.util.Objects;

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

    public InstanceSubscriber(Instance instance, Subscriber subscriber) {
        this.instance = instance;
        this.subscriber = subscriber;
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof InstanceSubscriber) {
            var is = (InstanceSubscriber) other;
            return Objects.equals(is.instance, this.instance) && Objects.equals(is.subscriber, this.subscriber);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance, this.subscriber);
    }
}
