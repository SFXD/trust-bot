package com.github.sfxd.trust.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private Json() {

    }

    public static <T> T deserialize(InputStream in, Class<T> clazz) {
        try {
            return MAPPER.readValue(in, clazz);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
