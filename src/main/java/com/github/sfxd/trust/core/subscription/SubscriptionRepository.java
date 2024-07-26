// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscription;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.users.User;

@Singleton
public class SubscriptionRepository extends Repository<Subscription> {

    SubscriptionRepository() {
        super(Subscription.class);
    }

    public Subscription findByInstanceAndUser(Instance instance, User user) {
        return this.query()
            .where()
            .eq("user", user)
            .eq("instance", instance)
            .findOne();
    }

    public Subscription findByKeyAndUsername(String key, String username) {
        return this.query()
            .where()
            .eq("user.username", username)
            .eq("instance.key", key)
            .findOne();
    }
}
