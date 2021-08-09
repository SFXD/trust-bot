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
