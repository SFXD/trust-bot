// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.github.sfxd.trust.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * Represents a user that has subscribe to notifications
 */
@Entity
public class Subscriber extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE)
    private List<InstanceSubscriber> instanceSubcribers;

    public Subscriber(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public Subscriber setUsername(String username) {
        this.username = username;
        return this;
    }

    public List<InstanceSubscriber> getInstanceSubscribers() {
        return this.instanceSubcribers;
    }

    public Subscriber setInstanceSubscribers(List<InstanceSubscriber> instanceSubscribers) {
        this.instanceSubcribers = instanceSubscribers;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Subscriber) {
            var subscriber = (Subscriber) other;
            return Objects.equals(this.username, subscriber.username);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
