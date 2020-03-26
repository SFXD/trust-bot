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

package com.github.sfxd.trust.services;

import java.util.List;
import java.util.Objects;

import javax.persistence.OptimisticLockException;

import com.github.sfxd.trust.model.AbstractEntity;

import io.ebean.Database;
import io.ebean.Query;

/** Base class for all of the entity services */
public abstract class AbstractEntityService<T extends AbstractEntity> {

    protected final Database db;
    protected final Class<T> clazz;

    public AbstractEntityService(Database db, Class<T> clazz) {
        Objects.requireNonNull(db);
        Objects.requireNonNull(clazz);

        this.db = db;
        this.clazz = clazz;
    }

    public List<T> insert(List<T> entities) throws DmlException {
        try {
            return this.save(entities);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    };

    public T insert(T entity) throws DmlException {
        try {
            return this.save(entity);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    };

    public List<T> update(List<T> entities) throws DmlException {
        try {
            return this.save(entities);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    };

    public T update(T entity) throws DmlException {
        try {
            return this.save(entity);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    }

    protected T save(T entity) {
        this.db.save(entity);
        return entity;
    }

    protected List<T> save(List<T> entities) {
        this.db.saveAll(entities);
        return entities;
    }

    public void delete(List<T> entities) throws DmlException {
        try {
            this.db.deleteAll(entities);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    }

    public void delete(T entity) throws DmlException {
        try {
            this.db.delete(entity);
        } catch (OptimisticLockException ex) {
            throw new DmlException(ex);
        }
    }

    public Query<T> findById(Long id) {
        return this.db.createQuery(this.clazz)
            .where()
            .idEq(id)
            .query();
    }

    /** A wrapper for exceptions from Hibernate */
    public static class DmlException extends Exception {
        private static final long serialVersionUID = -7822530728020542026L;

        public DmlException(Exception ex) {
            super(ex);
        }
    }
}
