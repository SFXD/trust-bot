package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Objects;

import com.github.sfxd.trust.model.AbstractEntity;
import com.github.sfxd.trust.repositories.AbstractEntityRepository;

/** Base class for all fo the entity services */
public abstract class AbstractEntityService<T extends AbstractEntity> {

    protected final AbstractEntityRepository<T> repository;

    public AbstractEntityService(AbstractEntityRepository<T> repository) {
        Objects.requireNonNull(repository);

        this.repository = repository;
    }

    public abstract List<T> insert(List<T> entities);
    public abstract T insert(T entity);
    public abstract List<T> update(List<T> entities);
    public abstract T update (T entity);
}
