package com.github.sfxd.trust.core.instancesubscribers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;
import java.util.stream.Collectors;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.instances.InstanceFinder;
import com.github.sfxd.trust.core.instances.InstanceService;
import com.github.sfxd.trust.core.subscribers.Subscriber;
import com.github.sfxd.trust.core.subscribers.SubscriberService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ebean.DB;
import net.dv8tion.jda.api.JDA;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceSubscriberFinderTests {

    private InstanceSubscriberFinder finder = new InstanceSubscriberFinder();
    private InstanceSubscriberService isService = new InstanceSubscriberService(DB.getDefault());
    private InstanceFinder instanceFinder = new InstanceFinder();
    private InstanceService instanceService = new InstanceService(
        DB.getDefault(),
        mock(JDA.class),
        this.finder,
        this.instanceFinder
    );
    private SubscriberService subService = new SubscriberService(DB.getDefault());

    @Test
    void it_should_find_subscribers_by_the_key_and_username() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.instanceService.insert(instance);

        var subscriber = new Subscriber("vips#7L");
        this.subService.insert(subscriber);

        this.isService.insert(new InstanceSubscriber(instance, subscriber));

        var found = this.finder.findByKeyAndUsername(instance.getKey(), subscriber.getUsername()).get();
        assertTrue(found != null);
        assertTrue(found.getInstance().getKey().equals(instance.getKey()));
        assertTrue(found.getSubscriber().getUsername().equals(subscriber.getUsername()));

        this.instanceService.delete(instance);
        this.subService.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_by_their_instance_id_and_subscriber_id() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.instanceService.insert(instance);

        var subscriber = new Subscriber("vips#7L");
        this.subService.insert(subscriber);

        this.isService.insert(new InstanceSubscriber(instance, subscriber));

        var found = this.finder.findByInstanceIdAndSubscriberId(instance.getId(), subscriber.getId()).get();
        assertTrue(found != null);
        assertEquals(instance.getId(), found.getInstance().getId());
        assertEquals(subscriber.getId(), found.getSubscriber().getId());

        this.instanceService.delete(instance);
        this.subService.delete(subscriber);
    }

    @Test
    void it_should_find_subscriptions_with_the_given_instances() throws Exception {
        var instance = new Instance().setKey("NA99");
        this.instanceService.insert(instance);

        var subscriber = new Subscriber("vips#7L");
        this.subService.insert(subscriber);

        this.isService.insert(new InstanceSubscriber(instance, subscriber));

        var found = this.finder.findByInstanceIdIn(Set.of(instance.getId())).collect(Collectors.toList());
        assertTrue(found.size() == 1);
        assertEquals(instance.getId(), found.get(0).getInstance().getId());

        this.instanceService.delete(instance);
        this.subService.delete(subscriber);
    }
}
