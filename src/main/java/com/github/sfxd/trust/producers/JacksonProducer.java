package com.github.sfxd.trust.producers;

import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class JacksonProducer {

    @Produces
    ObjectMapper produceObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.disable(
            DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        );

        return mapper;
    }
}
