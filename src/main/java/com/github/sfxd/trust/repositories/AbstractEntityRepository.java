package com.github.sfxd.trust.repositories;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.github.sfxd.trust.model.AbstractEntity;

/**
 * Base class for all repositories
 */
public abstract class AbstractEntityRepository<T extends AbstractEntity> {

    protected final EntityManager em;
    protected final Class<T> clazz;

    public AbstractEntityRepository(EntityManager em, Class<T> clazz) {
        Objects.requireNonNull(em);
        Objects.requireNonNull(clazz);

        this.em = em;
        this.clazz = clazz;
    }

    /**
     * Finds an entity by it's id
     *
     * @param id the entities id
     * @return Optional containing the found entity or Optional.empty
     */
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(this.em.find(this.clazz, id));
    }

    /** Saves the given entities */
    public List<T> save(List<T> entities) {
        return entities.stream()
            .map(this::save)
            .collect(Collectors.toList());
    }

    /** Saves the entity */
    public T save(T entity) {
        if (entity.isNew()) {
            this.em.persist(entity);
        } else {
            this.em.merge(entity);
        }

        return entity;
    }
}
