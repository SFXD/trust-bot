package com.github.sfxd.trust.core.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.sfxd.trust.users.User;
import com.github.sfxd.trust.users.UserRepository;
import org.junit.jupiter.api.Test;

class UserRepositoryTests {

    @Test
    void it_should_find_users_with_the_specified_username() throws Exception {
        var finder = new UserRepository();

        var subscriber = new User("vips#7L");
        finder.save(subscriber);

        User found = finder.findByUsername(subscriber.username());

        assertNotNull(found);
        assertEquals(found, subscriber);
        assertEquals(found.username(), subscriber.username());

        finder.delete(found);
    }
}
