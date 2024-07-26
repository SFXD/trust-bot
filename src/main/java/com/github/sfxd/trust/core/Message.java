package com.github.sfxd.trust.core;

import com.github.sfxd.trust.core.users.User;

public interface Message {
    User to();
    String body();
}
