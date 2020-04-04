package com.github.sfxd.trust.model.finders;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.services.SubscriberService;

import org.junit.jupiter.api.Test;

import io.ebean.DB;

class SubscriberFinderTests {

    @Test
    void it_should_find_users_with_the_specified_username() throws Exception {
        var finder = new SubscriberFinder();
        var subscriberService = new SubscriberService(DB.getDefault());

        var subscriber = new Subscriber("vips#7L");
        subscriberService.insert(subscriber);

        Optional<Subscriber> found = finder.findByUsername(subscriber.getUsername()).findOneOrEmpty();

        assertTrue(found.isPresent());
        assertTrue(found.get().equals(subscriber));
        assertTrue(found.get().getUsername().equals(subscriber.getUsername()));

        subscriberService.delete(found.get());
    }
}
