package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.github.sfxd.trust.model.Instance;

/**
 * Service for working with the {@link Instance} model.
 */
@Singleton
@Transactional
public class InstanceService extends AbstractEntityService<Instance> {

    @Inject
    public InstanceService(EntityManager em) {
        super(em, Instance.class);
    }

    /**
     * Finds an instance by its unique key (i.e. NA99, CS104)
     *
     * @param key the instance's unique key you want to find
     * @return An optional containing the instance if found or none if hibernate
     *         throws NoResultException
     */
    public Optional<Instance> findByKey(String key) {
        TypedQuery<Instance> query = this.em.createQuery("SELECT i FROM Instance i WHERE key = :key", this.clazz)
            .setParameter("key", key);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    /**
     * Finds all Instances whose key field is in the given set of keys.
     *
     * @param keys the keys you want to filter by
     * @return the matching instances
     */
    public List<Instance> findByKeyIn(Set<String> keys) {
        TypedQuery<Instance> query = this.em.createQuery("SELECT i FROM Instance i WHERE key IN :keys", this.clazz)
            .setParameter("keys", keys);

        return query.getResultList();
    }
}
