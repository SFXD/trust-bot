// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instanceusers;

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
public class InstanceUser extends Entity {

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private User user;

    public InstanceUser(Instance instance, User user) {
        this.instance = instance;
        this.user = user;
    }

    public Instance getInstance() {
        return this.instance;
    }

    public InstanceUser setInstance(Instance instance) {
        this.instance = instance;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public InstanceUser setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InstanceUser) {
            var is = (InstanceUser) other;
            return Objects.equals(is.instance, this.instance) && Objects.equals(is.user, this.user);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.instance, this.user);
    }
}
