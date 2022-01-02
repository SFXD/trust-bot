// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import java.util.Collection;
import java.util.List;

/** Base class for all of the entity services */
public abstract class EntityService<T extends Entity> {

    protected EntityService() {
    }

    protected abstract Repository<T> repository();

    public void insert(Collection<T> entities) {
        this.repository().insert(entities);
    }

    public void insert(T entity) {
        this.insert(List.of(entity));
    }

    public void update(Collection<T> entities) {
        this.repository().update(entities);
    }

    public void update(T entity) {
        this.update(List.of(entity));
    }

    public void delete(Collection<T> entities) {
        this.repository().delete(entities);
    }

    public void delete(T entity) {
        this.delete(List.of(entity));
    }
}
