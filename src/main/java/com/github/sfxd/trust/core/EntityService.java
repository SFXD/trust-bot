// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import java.util.List;

/** Base class for all of the entity services */
public abstract class EntityService<T extends Entity> {

    protected final Repository<T> repository;

    protected EntityService(Repository<T> repository) {
        this.repository = repository;
    }

    public List<T> insert(List<T> entities) {
        this.repository.insert(entities);

        return entities;
    }

    public T insert(T entity) {
        return this.insert(List.of(entity)).get(0);
    }

    public List<T> update(List<T> entities) {
        this.repository.update(entities);

        return entities;
    }

    public T update(T entity) {
        return this.update(List.of(entity)).get(0);
    }

    public void delete(List<T> entities) {
        this.repository.delete(entities);
    }

    public void delete(T entity) {
        this.delete(List.of(entity));
    }
}
