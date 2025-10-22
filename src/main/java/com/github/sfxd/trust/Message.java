package com.github.sfxd.trust;

import com.github.sfxd.trust.users.User;

public interface Message {
    User to();
    String body();
}
