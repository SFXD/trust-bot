// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instancesubscribers;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.subscribers.Subscriber;

/**
 * A junction between instances and their subscribers.
 */
@javax.persistence.Entity
public class InstanceSubscriber extends Entity {

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Subscriber subscriber;

    public InstanceSubscriber(Instance instance, Subscriber subscriber) {
        this.instance = instance;
        this.subscriber = subscriber;
    }

    public Instance getInstance() {
        return this.instance;
    }

    public InstanceSubscriber setInstance(Instance instance) {
        this.instance = instance;
        return this;
    }

    public Subscriber getSubscriber() {
        return this.subscriber;
    }

    public InstanceSubscriber setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        return this;
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
