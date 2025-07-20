// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.users;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.subscription.Subscription;

/**
 * Represents a user that has subscribe to notifications
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "users")
public class User extends Entity {

    @Column(unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Subscription> subscriptions;

    public User(String username) {
        this.username = username;
    }

    public String username() {
        return this.username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User subscriber) {
            return Objects.equals(this.username, subscriber.username);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
