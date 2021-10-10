// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instanceusers;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;

/** Finder for the InstanceUser model */
@Singleton
class InstanceUserRepository extends Repository<InstanceUser> {

    InstanceUserRepository() {
        super(InstanceUser.class);
    }

    /**
     * Finds a subscription by it's instances key and the the user's username
     *
     * @param key      the instance's key (e.g. NA99)
     * @param username discord user id
     * @return the query matching the subscriber
     */
    public Optional<InstanceUser> findByKeyAndUsername(String key, String username) {
        return this.query()
            .where()
            .eq("user.username", username)
            .and()
            .eq("instance.key", key)
            .findOneOrEmpty();
    }

    /**
     * Finds a subscription by its insance id and user's id
     *
     * @param instanceId   the id of the instance
     * @param userId the is of the subscriber
     * @return the query of matching subscribers
     */
    public Optional<InstanceUser> findByInstanceIdAndUserId(Long instanceId, Long userId) {
        return this.query()
            .where()
            .eq("user.id", userId)
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
    public Stream<InstanceUser> findByInstanceIdIn(Collection<Long> instanceIds) {
        return this.query()
            .where()
            .in("instance.id", instanceIds)
            .query()
            .findStream();
    }
}
