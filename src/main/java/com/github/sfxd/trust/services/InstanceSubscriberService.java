package com.github.sfxd.trust.services;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.github.sfxd.trust.model.InstanceSubscriber;

/**
 * Service for working with the {@link InstanceSubcriber} model
 */
@Singleton
@Transactional
public class InstanceSubscriberService extends AbstractEntityService<InstanceSubscriber> {

    @Inject
    public InstanceSubscriberService(EntityManager em) {
        super(em, InstanceSubscriber.class);
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
        TypedQuery<InstanceSubscriber> query = this.em.createQuery(
            "SELECT subscription " +
            "FROM InstanceSubscriber subscription " +
            "JOIN subscription.subscriber subscriber " +
            "JOIN subscription.instance instance " +
            "WHERE subscriber.username = :username AND instance.key = :key",
            this.clazz
        )
        .setParameter("username", username)
        .setParameter("key", key);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
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
        TypedQuery<InstanceSubscriber> query = this.em.createQuery(
            "SELECT subscription " +
            "FROM InstanceSubscriber subscription " +
            "JOIN subscription.subscriber subscriber " +
            "JOIN subscription.instance instance " +
            "WHERE subsriber.id = :subscriberId AND instance.id = :instanceId",
            this.clazz
        )
        .setParameter("subscriberId", subscriberId)
        .setParameter("instanceId", instanceId);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
