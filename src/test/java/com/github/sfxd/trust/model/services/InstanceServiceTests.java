package com.github.sfxd.trust.model.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;

import com.github.sfxd.trust.model.Instance;
import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.Subscriber;
import com.github.sfxd.trust.model.services.AbstractEntityService.DmlException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.ebean.DB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

@TestInstance(Lifecycle.PER_CLASS)
class InstanceServiceTests {

    private InstanceService instanceService;


    @BeforeAll
    void before() {
        this.instanceService = new InstanceService(
            DB.getDefault(),
            mock(JDA.class),
            new InstanceSubscriberService(DB.getDefault())
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void on_update_it_should_send_a_message_to_all_subscribers_if_the_instance_is_not_ok() throws Exception {
        // var jda = mock(JDA.class);
        // var instanceSubscriberService = mock(InstanceSubscriberService.class);
        // var action = mock(RestAction.class);
        // var channelAction = mock(RestAction.class);
        // var user = mock(User.class);
        // var channel = mock(PrivateChannel.class);
        // var messageAction = mock(MessageAction.class);

        // var instance = new Instance();
        // instance.setKey("NA99");
        // instance.setId(1L);
        // instance.setStatus("NOT_OK");
        // List<Instance> instances = List.of(instance);

        // var oldInstance = new Instance();
        // oldInstance.setKey("NA99");
        // oldInstance.setId(1L);
        // oldInstance.setStatus(Instance.STATUS_OK);

        // var subscriber = new Subscriber("vips#7L");
        // var instanceSubscribers = List.of(new InstanceSubscriber(oldInstance, subscriber));

        // when(em.createQuery(anyString(), any())).thenReturn(query);
        // when(query.setParameter(anyString(), any())).thenReturn(query);
        // when(query.getResultList()).thenReturn(List.of(oldInstance));
        // when(instanceSubscriberService.findByInstanceIdIn(Set.of(instance.getId())))
        //     .thenReturn(instanceSubscribers);
        // when(jda.retrieveUserById(anyString())).thenReturn(action);
        // doReturn(channelAction).when(user).openPrivateChannel();
        // doAnswer(invocation -> {
        //     var cb = (Consumer<PrivateChannel>) invocation.getArgument(0);
        //     cb.accept(channel);
        //     return null;
        // })
        // .when(channelAction).queue(any(Consumer.class));

        // doAnswer(invocation -> {
        //     var cb = (Consumer<User>) invocation.getArgument(0);
        //     cb.accept(user);
        //     return null;
        // })
        // .when(action).queue(any(Consumer.class));

        // when(channel.sendMessage(anyString())).thenReturn(messageAction);

        // var service = new InstanceService(em, jda, instanceSubscriberService);
        // service.update(instances);

        // verify(channel).sendMessage(anyString());
    }

    @Test
    void it_should_find_instances_whos_key_is_in_the_set() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA01");
        b.setKey("CS01");

        this.instanceService.insert(List.of(a, b));
        List<Instance> found = this.instanceService.findByKeyIn(Set.of(a.getKey())).findList();

        assertTrue(found.size() == 1);
        assertTrue(found.get(0).getKey().equals(a.getKey()));
    }

    @Test
    void it_should_find_instances_whos_id_is_in_the_set() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA02");
        b.setKey("CS02");

        this.instanceService.insert(List.of(a, b));
        List<Instance> found = this.instanceService.findByIdIn(Set.of(a.getId())).findList();

        assertTrue(found.size() == 1);
        assertTrue(found.get(0).getId().equals(a.getId()));
    }

    @Test
    void it_should_find_the_instance_which_the_matching_key() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA03");
        b.setKey("CS03");

        this.instanceService.insert(List.of(a, b));

        Instance found = this.instanceService.findByKey(a.getKey()).findOne();
        assertTrue(found.getKey().equals(a.getKey()));
    }

    @Test
    void it_should_return_an_empty_optional_when_no_result_is_found_by_key() throws DmlException {
        var a = new Instance();
        var b = new Instance();
        a.setKey("NA04");
        b.setKey("CS04");

        this.instanceService.insert(List.of(a, b));

        Optional<Instance> found = this.instanceService.findByKey("ZZ00").findOneOrEmpty();
        assertTrue(found.isEmpty());
    }
}
