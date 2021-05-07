package com.github.sfxd.trust.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.integrations.SalesforceTrustApiService;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

class SalesforceTrustApiServiceTests {

    @Test
    @SuppressWarnings("unchecked")
    void it_should_throw_when_get_previews_does_not_return_200() {
        var httpClient = mock(HttpClient.class);
        var response = mock(HttpResponse.class);

        when(httpClient.sendAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(response));
        when(response.statusCode()).thenReturn(500);

        var service = new SalesforceTrustApiService(httpClient);

        var f = service.getInstancesStatusPreview();

        assertTrue(f.isCompletedExceptionally());
    }

    @Test
    @SuppressWarnings("unchecked")
    void it_should_return_the_serialized_response_from_get_status_previews() throws Exception {
        var httpClient = mock(HttpClient.class);
        var objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        var response = mock(HttpResponse.class);
        var instances = List.of(new Instance().setKey("NA99"));

        when(httpClient.sendAsync(any(), any())).thenReturn(CompletableFuture.completedFuture(response));
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(
            IOUtils.toInputStream(objectMapper.writeValueAsString(instances), Charset.defaultCharset())
        );

        var service = new SalesforceTrustApiService(httpClient);

        var f = service.getInstancesStatusPreview();
        assertEquals(instances, f.join());
    }
}
