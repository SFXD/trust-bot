// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instancesubscribers;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;

/** Finder for the InstanceSubscriber model */
@Singleton
class InstanceSubscriberRepository extends Repository<InstanceSubscriber> {

    InstanceSubscriberRepository() {
        super(InstanceSubscriber.class);
    }

    /**
     * Finds a subscription by it's instances key and the the subscriber's username
     *
     * @param key      the instance's key (e.g. NA99)
     * @param username discord user id
     * @return the query matching the subscriber
     */
    public Optional<InstanceSubscriber> findByKeyAndUsername(String key, String username) {
        return this.query()
            .where()
            .eq("subscriber.username", username)
            .and()
            .eq("instance.key", key)
            .findOneOrEmpty();
    }

    /**
     * Finds a subscription by its insance id and subscriber's id
     *
     * @param instanceId   the id of the instance
     * @param subscriberId the is of the subscriber
     * @return the query of matching subscribers
     */
    public Optional<InstanceSubscriber> findByInstanceIdAndSubscriberId(Long instanceId, Long subscriberId) {
        return this.query()
            .where()
            .eq("subscriber.id", subscriberId)
            .and()
            .eq("instance.id", instanceId)
            .findOneOrEmpty();
    }

    /**
     * Finds all subscribers who's instance is in the provided instance ids
     *
     * @param instanceIds the instances you want to filter by
     * @return the matching subscribers
     */
    public Stream<InstanceSubscriber> findByInstanceIdIn(Collection<Long> instanceIds) {
        return this.query()
            .where()
            .in("instance.id", instanceIds)
            .query()
            .findStream();
    }
}
