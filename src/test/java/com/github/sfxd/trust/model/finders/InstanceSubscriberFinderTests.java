package com.github.sfxd.trust.model.finders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.services.InstanceService;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;
import com.github.sfxd.trust.model.services.SubscriberService;

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

        var found = this.finder.findByKeyAndUsername(instance.getKey(), subscriber.getUsername()).findOne();
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

        var found = this.finder.findByInstanceIdAndSubscriberId(instance.getId(), subscriber.getId()).findOne();
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

        var found = this.finder.findByInstanceIdIn(Set.of(instance.getId())).findList();
        assertTrue(found.size() == 1);
        assertEquals(instance.getId(), found.get(0).getInstance().getId());

        this.instanceService.delete(instance);
        this.subService.delete(subscriber);
    }
}
