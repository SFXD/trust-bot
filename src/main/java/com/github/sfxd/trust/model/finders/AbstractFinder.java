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

package com.github.sfxd.trust.model.finders;


import com.github.sfxd.trust.model.AbstractEntity;

import io.ebean.DB;
import io.ebean.Query;

/** The base of all finders */
abstract class AbstractFinder<T extends AbstractEntity> {

    protected final Class<T> clazz;

    protected AbstractFinder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Finds an entity by its id
     * @param id the id of the entity you want to find
     * @return an optional containing the entity or empty
     */
    public Query<T> findById(Long id) {
        return DB.getDefault().createQuery(this.clazz)
            .where()
            .idEq(id)
            .query();
    }
}
