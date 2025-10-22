// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.users;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import com.github.sfxd.trust.Entity;
import com.github.sfxd.trust.instances.Instance;

import static java.util.Objects.requireNonNull;

/**
 * A junction between instances and their subscribers.
 */
@jakarta.persistence.Entity
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
