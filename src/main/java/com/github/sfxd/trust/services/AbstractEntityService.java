package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.github.sfxd.trust.model.AbstractEntity;

/** Base class for all of the entity services */
public abstract class AbstractEntityService<T extends AbstractEntity> {

    protected final EntityManager em;
    protected final Class<T> clazz;

    public AbstractEntityService(EntityManager em, Class<T> clazz) {
        Objects.requireNonNull(em);
        Objects.requireNonNull(clazz);

        this.em = em;
        this.clazz = clazz;
    }

    public List<T> insert(List<T> entities) {
        return this.save(entities);
    };

    public T insert(T entity) {
        return this.save(entity);
    };

    public List<T> update(List<T> entities) {
        return this.save(entities);
    };

    public T update(T entity) {
        return this.save(entity);
    }

    protected T save(T entity) {
        if (entity.isNew()) {
            this.em.persist(entity);
            return entity;
        } else {
            return this.em.merge(entity);
        }
    }

    protected List<T> save(List<T> entities) {
        return entities.stream()
            .map(this::save)
            .collect(Collectors.toList());
    }

    public void delete(List<T> entities) {
        for (T t : entities) {
            this.delete(t);
        }
    }

    public void delete(T entity) {
        this.em.remove(entity);
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(this.em.find(this.clazz, id));
    }
}
