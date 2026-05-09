// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust;

import java.util.concurrent.CountDownLatch;

import io.avaje.inject.BeanScope;
import io.ebean.Database;

public class Main {
    static void main(String[] args) throws Exception {
        try (var scope = BeanScope.builder().build()) {
            var _ = scope.get(Database.class);
            var latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
            latch.await();
        }
    }
}
