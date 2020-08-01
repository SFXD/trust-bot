package com.github.sfxd.trust.listeners;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.github.sfxd.trust.model.InstanceSubscriber;
import com.github.sfxd.trust.model.finders.InstanceSubscriberFinder;
import com.github.sfxd.trust.model.query.QInstanceSubscriber;
import com.github.sfxd.trust.model.services.InstanceSubscriberService;

import org.junit.jupiter.api.Test;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

class UnsubscribeBotCommandTests {

    @Test
    void it_should_delete_the_subscription_if_found() throws Exception {
        var event = mock(MessageReceivedEvent.class);
        var isFinder = mock(InstanceSubscriberFinder.class);
        var isService = mock(InstanceSubscriberService.class);
        var key = "NA99";
        var username = "george";
        var user = mock(User.class);
        var subscription = new InstanceSubscriber(null, null);
        var query = mock(QInstanceSubscriber.class);
        var message = mock(Message.class);

        @SuppressWarnings("unchecked")
        var action = (RestAction<Void>) mock(RestAction.class);

        when(event.getAuthor()).thenReturn(user);
        when(event.getMessage()).thenReturn(message);
        when(message.addReaction(anyString())).thenReturn(action);
        when(user.getId()).thenReturn(username);
        when(isFinder.findByKeyAndUsername(key, username)).thenReturn(query);
        when(query.findOneOrEmpty()).thenReturn(Optional.of(subscription));

        var command = new UnsubscribeBotCommand(event, key, isFinder, isService);
        command.run();

        verify(isService).delete(subscription);
    }
}
