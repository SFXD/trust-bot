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

package com.github.sfxd.trust.core.instancesubscribers;

import java.util.Objects;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.subscribers.Subscriber;

/**
 * A junction between instances and their subscribers.
 */
@javax.persistence.Entity
public class InstanceSubscriber extends Entity {

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Instance instance;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
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
