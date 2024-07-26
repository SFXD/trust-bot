// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscription;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.users.User;

/**
 * A junction between instances and their subscribers.
 */
@javax.persistence.Entity
public class Subscription extends Entity {

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private User user;

    public Subscription(Instance instance, User user) {
        this.instance = instance;
        this.user = user;
    }

    public Instance getInstance() {
        return this.instance;
    }

    public Subscription setInstance(Instance instance) {
        this.instance = instance;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public Subscription setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Subscription subscription) {
            return Objects.equals(subscription.instance, this.instance)
                && Objects.equals(subscription.user, this.user);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance, this.user);
    }
}
