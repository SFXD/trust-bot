package com.github.sfxd.trust.quarkus;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

class ObjectMapperCustomizerTests {

    @Test
    void it_should_disable_failing_on_unknown_properties() {
        var mapper = mock(ObjectMapper.class);
        var customizer = new ObjectMapperCustomizer();
        customizer.customize(mapper);

        verify(mapper).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
