// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instances;

import java.util.Collection;
import java.util.Collections;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;

/** Finder for the Instance model */
@Singleton
public class InstanceRepository extends Repository<Instance> {
    private static final String KEY = "key";

    InstanceRepository() {
        super(Instance.class);
    }

    /**
     * Finds an instance by its unique key (i.e. NA99, CS104)
     *
     * @param key the instance's unique key you want to find
     * @return the matching instances
     */
    public Instance findByKey(String key) {
        return this.query()
            .where()
            .eq(KEY, key)
            .findOne();
    }

    /**
     * Finds all Instances whose key field is in the given set of keys.
     *
     * @param keys the keys you want to filter by
     * @return the matching instances
     */
    public Collection<Instance> findByKeyIn(Collection<String> keys) {
        var query = this.query()
            .fetch("subscriptions")
            .where()
            .in(KEY, keys)
            .query();

        return Collections.unmodifiableList(query.findList());
    }

    /**
     * Finds all Instances whose id field is in the given set of ids
     *
     * @param ids the ids you want to filter by
     * @return the matching instances
     */
    public Collection<Instance> findByIdIn(Collection<Long> ids) {
        var query = this.query()
            .where()
            .idIn(ids)
            .query();

        return Collections.unmodifiableList(query.findList());
    }
}
