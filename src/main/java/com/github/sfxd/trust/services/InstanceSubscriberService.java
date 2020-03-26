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

package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;

import com.github.sfxd.trust.model.InstanceSubscriber;

import io.ebean.Database;
import io.ebean.annotation.Transactional;

/**
 * Service for working with the {@link InstanceSubcriber} model
 */
@Singleton
@Transactional
public class InstanceSubscriberService extends AbstractEntityService<InstanceSubscriber> {

    @Inject
    public InstanceSubscriberService(Database db) {
        super(db, InstanceSubscriber.class);
    }

    /**
     * Finds a subscription by it's instances key and the the subscriber's username
     *
     * @param key      the instance's key (e.g. NA99)
     * @param username discord user name as a tag (e.g. vipasana#0267)
     * @return An optional containing the instance or empty if hibernate throws
     *         {@link NoResultException}
     */
    public Optional<InstanceSubscriber> findByKeyAndUsername(String key, String username) {
        // TypedQuery<InstanceSubscriber> query = this.em.createQuery(
        //     "SELECT subscription " +
        //     "FROM InstanceSubscriber subscription " +
        //     "JOIN subscription.subscriber subscriber " +
        //     "JOIN subscription.instance instance " +
        //     "WHERE subscriber.username = :username AND instance.key = :key",
        //     this.clazz
        // )
        // .setParameter("username", username)
        // .setParameter("key", key);

        // try {
        //     return Optional.of(query.getSingleResult());
        // } catch (NoResultException ex) {
        //     return Optional.empty();
        // }

        return null;
    }

    /**
     * Finds a subscription by its insance id and subscriber's id
     *
     * @param instanceId   the id of the instance
     * @param subscriberId the is of the subscriber
     * @return An optional containing the instance or empty if hibernate throws
     *         {@link NoResultException}
     */
    public Optional<InstanceSubscriber> findByInstanceIdAndSubscriberId(Long instanceId, Long subscriberId) {
        // TypedQuery<InstanceSubscriber> query = this.em.createQuery(
        //     "SELECT subscription " +
        //     "FROM InstanceSubscriber subscription " +
        //     "JOIN subscription.subscriber subscriber " +
        //     "JOIN subscription.instance instance " +
        //     "WHERE subsriber.id = :subscriberId AND instance.id = :instanceId",
        //     this.clazz
        // )
        // .setParameter("subscriberId", subscriberId)
        // .setParameter("instanceId", instanceId);

        // try {
        //     return Optional.of(query.getSingleResult());
        // } catch (NoResultException ex) {
        //     return Optional.empty();
        // }
        return null;
    }

    /**
     * Finds all subscribers who's instance is in the provided instance ids
     *
     * @param instanceIds the instances you want to filter by
     * @return the matching subscribers
     */
    public List<InstanceSubscriber> findByInstanceIdIn(Set<Long> instanceIds) {
        // TypedQuery<InstanceSubscriber> query = this.em.createQuery(
        //     "SELECT subscription " +
        //     "FROM InstanceSubscriber subscription " +
        //     "JOIN subscription.instance instance " +
        //     "WHERE instance.id IN :instanceIds",
        //     this.clazz
        // )
        // .setParameter("instanceIds", instanceIds);

        // return query.getResultList();
        return null;
    }
}
