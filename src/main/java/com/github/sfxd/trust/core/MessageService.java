// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core;

import com.github.sfxd.trust.core.subscribers.Subscriber;

public interface MessageService {
    void sendMessage(Subscriber user, String message);
}
