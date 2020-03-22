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

package com.github.sfxd.trust.services.web;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.github.sfxd.trust.model.Instance;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Service for interating with the Salesforce trust api.
 * This implements only a subset of the functionality of the api.
 *
 * @see https://api.status.salesforce.com/v1/docs/
 */
@Path("/v1")
@RegisterRestClient
@ApplicationScoped
public interface SalesforceTrustApiService {

    @GET
    @Path("/instances/status/preview")
    @Produces("application/json")
    List<Instance> getInstancesStatusPreview();

}
