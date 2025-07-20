// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.subscription;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.subscription.query.QSubscription;
import com.github.sfxd.trust.core.users.User;

@Singleton
public class SubscriptionRepository extends Repository<Subscription> {

    SubscriptionRepository() {
        super(Subscription.class);
    }

    public Subscription findByInstanceAndUser(Instance instance, User user) {
        return new QSubscription()
            .user.eq(user)
            .instance.eq(instance)
            .findOne();
    }

    public Subscription findByKeyAndUsername(String key, String username) {
        return new QSubscription()
            .user.username.eq(username)
            .instance.key.eq(key)
            .findOne();
    }
}
