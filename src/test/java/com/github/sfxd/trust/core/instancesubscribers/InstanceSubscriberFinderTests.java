package com.github.sfxd.trust.core.instancesubscribers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.subscribers.Subscriber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ebean.DB;
import io.ebean.Database;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceSubscriberFinderTests {

    private final InstanceSubscriberRepository finder = new InstanceSubscriberRepository();
    private final Database db = DB.getDefault();

    @Test
    void it_should_find_subscribers_by_the_key_and_username() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new Subscriber("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceSubscriber(instance, subscriber)));

        var found = this.finder.findByKeyAndUsername(instance.getKey(), subscriber.getUsername()).get();
        assertNotNull(found);
        assertEquals(found.getInstance().getKey(), instance.getKey());
        assertEquals(found.getSubscriber().getUsername(), subscriber.getUsername());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_by_their_instance_id_and_subscriber_id() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new Subscriber("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceSubscriber(instance, subscriber)));

        var found = this.finder.findByInstanceIdAndSubscriberId(instance.getId(), subscriber.getId()).get();
        assertNotNull(found);
        assertEquals(instance.getId(), found.getInstance().getId());
        assertEquals(subscriber.getId(), found.getSubscriber().getId());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_with_the_given_instances() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.db.save(instance);

        var subscriber = new Subscriber("vips#7L");
        this.db.save(subscriber);

        this.finder.insert(List.of(new InstanceSubscriber(instance, subscriber)));

        var found = this.finder.findByInstanceIdIn(Set.of(instance.getId())).collect(Collectors.toList());
        assertEquals(1, found.size());
        assertEquals(instance.getId(), found.get(0).getInstance().getId());

        this.db.delete(instance);
        this.db.delete(subscriber);
    }
}
