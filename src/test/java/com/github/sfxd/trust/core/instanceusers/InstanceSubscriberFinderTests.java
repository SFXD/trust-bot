package com.github.sfxd.trust.core.instanceusers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.users.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ebean.DB;
import io.ebean.Database;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceSubscriberFinderTests {

    private final InstanceUserRepository finder = new InstanceUserRepository();
    private final Database db = DB.getDefault();

    @Test
    void it_should_find_subscribers_by_the_key_and_username() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new User("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceUser(instance, subscriber)));

        var found = this.finder.findByKeyAndUsername(instance.getKey(), subscriber.getUsername()).get();
        assertNotNull(found);
        assertEquals(found.getInstance().getKey(), instance.getKey());
        assertEquals(found.getUser().getUsername(), subscriber.getUsername());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_by_their_instance_id_and_subscriber_id() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new User("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceUser(instance, subscriber)));

        var found = this.finder.findByInstanceIdAndUserId(instance.getId(), subscriber.getId()).get();
        assertNotNull(found);
        assertEquals(instance.getId(), found.getInstance().getId());
        assertEquals(subscriber.getId(), found.getUser().getId());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_with_the_given_instances() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new User("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceUser(instance, subscriber)));

        var found = this.finder.findByInstanceIdIn(Set.of(instance.getId())).collect(Collectors.toList());
        assertEquals(1, found.size());
        assertEquals(instance.getId(), found.get(0).getInstance().getId());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }
}
