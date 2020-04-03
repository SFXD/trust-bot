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

package com.github.sfxd.trust.model.finders;

import java.util.Set;

import javax.inject.Singleton;

import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.query.QInstanceSubscriber;

/** Finder for the InstanceSubscriber model */
@Singleton
public class InstanceSubscriberFinder extends AbstractFinder<InstanceSubscriber> {

    public InstanceSubscriberFinder() {
        super(InstanceSubscriber.class);
    }

    /**
     * Finds a subscription by it's instances key and the the subscriber's username
     *
     * @param key      the instance's key (e.g. NA99)
     * @param username discord user id
     * @return the query matching the subscriber
     */
    public QInstanceSubscriber findByKeyAndUsername(String key, String username) {
        return new QInstanceSubscriber()
            .where()
            .subscriber.username.eq(username)
            .and()
            .instance.key.eq(key);
    }

    /**
     * Finds a subscription by its insance id and subscriber's id
     *
     * @param instanceId   the id of the instance
     * @param subscriberId the is of the subscriber
     * @return the query of matching subscribers
     */
    public QInstanceSubscriber findByInstanceIdAndSubscriberId(Long instanceId, Long subscriberId) {
        return new QInstanceSubscriber()
            .where()
            .subscriber.id.eq(subscriberId)
            .and()
            .instance.id.eq(instanceId);
    }

    /**
     * Finds all subscribers who's instance is in the provided instance ids
     *
     * @param instanceIds the instances you want to filter by
     * @return the matching subscribers
     */
    public QInstanceSubscriber findByInstanceIdIn(Set<Long> instanceIds) {
        return new QInstanceSubscriber()
            .where()
            .instance.id.in(instanceIds);
    }
}
