// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import com.github.sfxd.trust.core.users.User;

public interface MessageService {
    void sendMessage(User user, String message);
}
