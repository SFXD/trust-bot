package com.github.sfxd.trust.core.users;

import com.github.sfxd.trust.core.instances.Instance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.sfxd.trust.core.instances.Instance.Environment.PRODUCTION;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
    private User user;
    private Instance instance;

    @BeforeEach
    public void before() {
        user = new User("eddie");
        instance = new Instance("NA01", PRODUCTION);
        instance.setId(1L);
    }

    @Test
    public void isSubscribedShouldReturnTrueWhenSubscribed() {
        user.subscribe(instance);
        assertTrue(user.isSubscribed(instance));
    }

    @Test
    public void unsubscribeShouldUnsubscribeTheUser() {
        user.unsubscribe(instance);
        assertFalse(user.isSubscribed(instance));
    }
}
