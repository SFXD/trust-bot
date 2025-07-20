// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.users;

import javax.inject.Singleton;

import com.github.sfxd.trust.core.Repository;
import com.github.sfxd.trust.core.users.query.QUser;

@Singleton
public class UserRepository extends Repository<User> {

    public UserRepository() {
        super(User.class);
    }

    /**
     * Finds a subscriber by their unique username.
     *
     * @param username the user you want to find
     * @return An Optional containing the subscriber or empty if hibernate throws
     *         NoResultException
     */
    public User findByUsername(String username) {
        return new QUser()
            .username.eq(username)
            .findOne();
    }
}
