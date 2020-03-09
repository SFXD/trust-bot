package com.github.sfxd.trust.services;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import com.github.sfxd.trust.model.Subscriber;

/**
 * Service for interacting with the {@link Subscriber} model.
 */
@ApplicationScoped
@Transactional
public class SubscriberService extends AbstractEntityService<Subscriber> {

    public SubscriberService(EntityManager em) {
        super(em, Subscriber.class);
    }

    /**
     * Finds a subscriber by their unique username.
     *
     * @param username the user you want to find
     * @return An Optional containing the subscriber or empty if hibernate throws
     *         NoResultException
     */
    public Optional<Subscriber> findByUsername(String username) {
        var query = this.em.createQuery("SELECT s FROM Subscriber s WHERE username = :username", this.clazz)
            .setParameter("username", username);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
