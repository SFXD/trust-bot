// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.users;

import javax.inject.Singleton;

import com.github.sfxd.trust.users.query.QUser;
import io.ebean.DB;

@Singleton
public class UserRepository {
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

    public void save(User user) throws DuplicateUserException {
        var existing = this.findByUsername(user.username());
        if (existing != null && !existing.equals(user)) {
            throw new DuplicateUserException();
        }

        DB.save(user);
    }

    public void delete(User user) {
        DB.delete(user);
    }
}
