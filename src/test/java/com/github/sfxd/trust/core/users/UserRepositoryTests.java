package com.github.sfxd.trust.core.users;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class UserRepositoryTests {

    @Test
    void it_should_find_users_with_the_specified_username() throws Exception {
        var finder = new UserRepository();

        var subscriber = new User("vips#7L");
        finder.insert(List.of(subscriber));

        User found = finder.findByUsername(subscriber.getUsername());

        assertNotNull(found);
        assertEquals(found, subscriber);
        assertEquals(found.getUsername(), subscriber.getUsername());

        finder.delete(List.of(found));
    }
}
