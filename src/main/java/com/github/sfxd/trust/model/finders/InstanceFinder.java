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

import java.util.Set;

import javax.inject.Singleton;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.query.QInstance;

/** Finder for the Instance model */
@Singleton
public class InstanceFinder extends AbstractFinder<Instance> {

    public InstanceFinder() {
        super(Instance.class);
    }

    /**
     * Finds an instance by its unique key (i.e. NA99, CS104)
     *
     * @param key the instance's unique key you want to find
     * @return the matching instances
     */
    public QInstance findByKey(String key) {
        return new QInstance()
            .where()
            .key
            .eq(key);
    }

    /**
     * Finds all Instances whose key field is in the given set of keys.
     *
     * @param keys the keys you want to filter by
     * @return the matching instances
     */
    public QInstance findByKeyIn(Set<String> keys) {
        return new QInstance()
            .where()
            .key
            .in(keys);
    }

    /**
     * Finds all Instances whose id field is in the given set of ids
     *
     * @param ids the ids you want to filter by
     * @return the matching instances
     */
    public QInstance findByIdIn(Set<Long> ids) {
        return new QInstance()
            .where()
            .id
            .in(ids);
    }
}
