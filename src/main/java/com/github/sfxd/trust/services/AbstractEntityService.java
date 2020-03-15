package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

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

    public List<T> insert(List<T> entities) throws DmlException {
        try {
            return this.save(entities);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DmlException(ex);
        }
    };

    public T insert(T entity) throws DmlException {
        try {
            return this.save(entity);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DmlException(ex);
        }
    };

    public List<T> update(List<T> entities) throws DmlException {
        try {
            return this.save(entities);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DmlException(ex);
        }
    };

    public T update(T entity) throws DmlException {
        try {
            return this.save(entity);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            throw new DmlException(ex);
        }
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

    public void delete(List<T> entities) throws DmlException {
        for (T t : entities) {
            this.delete(t);
        }
    }

    public void delete(T entity) throws DmlException {
        try {
            this.em.remove(entity);
        } catch (TransactionRequiredException | IllegalArgumentException ex) {
            throw new DmlException(ex);
        }
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(this.em.find(this.clazz, id));
    }

    /** A wrapper for exceptions from Hibernate */
    public static class DmlException extends Exception {
        private static final long serialVersionUID = -7822530728020542026L;

        public DmlException(Exception ex) {
            super(ex);
        }
    }
}
