// trust-bot a discord bot to watch the salesforce trust api.
// Copyright (C) 2020 George Doenlen

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package com.github.sfxd.trust.integrations;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.sfxd.trust.core.instances.Instance;
import com.github.sfxd.trust.util.Json;

/**
 * Service for interating with the Salesforce trust api. This implements only a
 * subset of the functionality of the api.
 *
 * @see https://api.status.salesforce.com/v1/docs/
 */
@ApplicationScoped
public class SalesforceTrustApiService {
    private static final String BASE_URI = "https://api.status.salesforce.com/v1";

    private final HttpClient httpClient;

    @Inject
    public SalesforceTrustApiService(HttpClient httpClient) {
        Objects.requireNonNull(httpClient);

        this.httpClient = httpClient;
    }

    /**
     * Gets the preview status of all instances from the api.
     *
     * @return the list of instances or empty optional if the request did not return
     *         200
     */
    public CompletableFuture<List<Instance>> getInstancesStatusPreview() {
        var request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(BASE_URI + "/instances/status/preview"))
            .header("Accept", "application/json")
            .build();

        return this.httpClient.sendAsync(request, BodyHandlers.ofInputStream()).thenApply(response -> {
            if (response.statusCode() != 200) {
                throw new InvalidResponseException(response.statusCode());
            }

            return Arrays.asList(Json.deserialize(response.body(), Instance[].class));
        });
    }
}
