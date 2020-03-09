package com.github.sfxd.trust.services;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.github.sfxd.trust.model.InstanceSubscriber;

/**
 * Service for working with the {@link InstanceSubcriber} model
 */
@ApplicationScoped
@Transactional
public class InstanceSubscriberService extends AbstractEntityService<InstanceSubscriber> {

    @Inject
    public InstanceSubscriberService(EntityManager em) {
        super(em, InstanceSubscriber.class);
    }

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
}
