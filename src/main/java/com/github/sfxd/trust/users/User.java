// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.users;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import com.github.sfxd.trust.Entity;
import com.github.sfxd.trust.instances.Instance;

import static java.util.Objects.requireNonNull;

/// Represents a user that has subscribed to notifications
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "users")
public class User extends Entity {

    @Column(unique = true, nullable = false, columnDefinition = "character varying")
    private final String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Subscription> subscriptions;

    public User(String username) {
        this.username = requireNonNull(username);
    }

    public String username() {
        return this.username;
    }

    public void subscribe(Instance instance) {
        if (this.isSubscribed(instance)) {
            return;
        }

        this.subscriptions.add(new Subscription(instance, this));
    }

    public boolean isSubscribed(Instance instance) {
        return this.subscriptions.stream()
            .map(Subscription::instance)
            .anyMatch(instance::equals);
    }

    public void unsubscribe(Instance instance) {
        this.subscriptions.stream()
            .filter(subscription -> subscription.instance().equals(instance))
            .findFirst()
            .ifPresent(this.subscriptions::remove);
    }
}
