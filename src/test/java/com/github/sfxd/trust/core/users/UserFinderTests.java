package com.github.sfxd.trust.core.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class UserFinderTests {

    @Test
    void it_should_find_users_with_the_specified_username() throws Exception {
        var finder = new UserFinder();

        var subscriber = new User("vips#7L");
        finder.insert(List.of(subscriber));

        Optional<User> found = finder.findByUsername(subscriber.getUsername());

        assertTrue(found.isPresent());
        assertEquals(found.get(), subscriber);
        assertEquals(found.get().getUsername(), subscriber.getUsername());

        finder.delete(List.of(found.get()));
    }
}
