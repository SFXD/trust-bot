package com.github.sfxd.trust.core.instanceusers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.core.users.User;
import com.github.sfxd.trust.core.users.UserService;

import org.junit.jupiter.api.Test;

class InstanceUserServiceTests {

    @Test
    void it_should_insert_new_users() {
        var userService = mock(UserService.class);
        var iuRepo = mock(InstanceUserRepository.class);
        var isService = new InstanceUserService(iuRepo, userService);
        var user = new User("");
        var instance = new Instance();
        instance.setId(1L);
        var instanceUser = new InstanceUser(instance, user);

        isService.insert(instanceUser);

        verify(userService).insert(List.of(user));
    }
}
