// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
