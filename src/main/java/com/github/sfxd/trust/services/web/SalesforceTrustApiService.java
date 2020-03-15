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
@RegisterRestClient(configKey = "salesforce-trust-api")
@ApplicationScoped
public interface SalesforceTrustApiService {

    @GET
    @Path("/instances/status/preview")
    @Produces("application/json")
    List<Instance> getInstancesStatusPreview();

}
