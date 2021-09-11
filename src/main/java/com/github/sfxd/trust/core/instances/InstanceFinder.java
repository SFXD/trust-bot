// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instances;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;

/** Finder for the Instance model */
@Singleton
class InstanceFinder extends Repository<Instance> {
    private static final String KEY = "key";

    InstanceFinder() {
        super(Instance.class);
    }

    /**
     * Finds an instance by its unique key (i.e. NA99, CS104)
     *
     * @param key the instance's unique key you want to find
     * @return the matching instances
     */
    public Optional<Instance> findByKey(String key) {
        return this.query()
            .where()
            .eq(KEY, key)
            .findOneOrEmpty();
    }

    /**
     * Finds all Instances whose key field is in the given set of keys.
     *
     * @param keys the keys you want to filter by
     * @return the matching instances
     */
    public Stream<Instance> findByKeyIn(Collection<String> keys) {
        return this.query()
            .where()
            .in(KEY, keys)
            .query()
            .findStream();
    }

    /**
     * Finds all Instances whose id field is in the given set of ids
     *
     * @param ids the ids you want to filter by
     * @return the matching instances
     */
    public Stream<Instance> findByIdIn(Collection<Long> ids) {
        return this.query()
            .where()
            .idIn(ids)
            .query()
            .findStream();
    }
}
