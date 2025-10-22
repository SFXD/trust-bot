// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust;

import java.util.concurrent.CountDownLatch;

import io.avaje.inject.BeanScope;

public class Main {
    static void main(String[] args) throws Exception {
        try (var _ = BeanScope.builder().build()) {
            var latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
            latch.await();
        }
    }
}
