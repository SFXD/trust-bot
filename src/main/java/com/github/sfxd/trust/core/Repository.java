// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import java.util.Collection;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.Query;

public abstract class Repository<T extends Entity> {

    protected final Class<T> clazz;
    protected final Database database;

    protected Repository(Class<T> clazz) {
        this.clazz = clazz;
        this.database = DB.getDefault();
    }

    public T findById(Long id) {
        return this.query()
            .where()
            .idEq(id)
            .findOne();
    }

    protected Query<T> query() {
        return this.database.createQuery(this.clazz);
    }

    public void insert(Collection<T> entities) {
        this.database.insertAll(entities);
    }

    public void insert(T entity) {
        this.database.insert(entity);
    }

    public void update(Collection<T> entities) {
        this.database.updateAll(entities);
    }

    public void update(T entity) {
        this.database.update(entity);
    }

    public void delete(Collection<T> entities) {
        this.database.deleteAll(entities);
    }

    public void delete(T entity) {
        this.database.delete(entity);
    }

    public void save(Collection<T> entities) {
        this.database.saveAll(entities);
    }

    public void save(T entity) {
        this.database.save(entity);
    }
}
