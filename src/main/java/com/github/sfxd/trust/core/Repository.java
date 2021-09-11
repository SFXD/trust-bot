// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import java.util.Collection;
import java.util.Optional;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.Query;

/** The base of all finders */
public abstract class Repository<T extends Entity> {

    protected final Class<T> clazz;
    protected final Database database;

    protected Repository(Class<T> clazz) {
        this.clazz = clazz;
        this.database = DB.getDefault();
    }

    /**
     * Finds an entity by its id
     * @param id the id of the entity you want to find
     * @return an optional containing the entity or empty
     */
    public Optional<T> findById(Long id) {
        return this.query()
            .where()
            .idEq(id)
            .findOneOrEmpty();
    }

    protected Query<T> query() {
        return DB.getDefault().createQuery(this.clazz);
    }

    public void insert(Collection<T> entities) {
        this.database.insertAll(entities);
    }

    public void update(Collection<T> entities) {
        this.database.updateAll(entities);
    }

    public void delete(Collection<T> entities) {
        this.database.deleteAll(entities);
    }
}
