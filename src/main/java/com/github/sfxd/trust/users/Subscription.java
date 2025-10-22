// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.users;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.github.sfxd.trust.Entity;
import com.github.sfxd.trust.instances.Instance;

import static java.util.Objects.requireNonNull;

/**
 * A junction between instances and their subscribers.
 */
@javax.persistence.Entity
public class Subscription extends Entity {

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private final Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private final User user;

    Subscription(Instance instance, User user) {
        this.instance = requireNonNull(instance);
        this.user = requireNonNull(user);
    }

    public Instance instance() {
        return this.instance;
    }

    public User user() {
        return this.user;
    }
}
