// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.util;

import io.avaje.jsonb.Jsonb;

import java.io.InputStream;


public class Json {
    private static final Jsonb JSONB = Jsonb.builder().build();
    private Json() {}

    public static <T> T deserialize(InputStream in, Class<T> clazz) {
        return JSONB.type(clazz).fromJson(in);
    }
}
