package com.github.sfxd.trust.util;

import com.github.sfxd.trust.Message;
import com.github.sfxd.trust.users.User;

public record SimpleMessage(User to, String body) implements Message {
}
