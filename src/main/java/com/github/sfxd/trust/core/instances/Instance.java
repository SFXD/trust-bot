// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instances;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.subscription.Subscription;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an SFDC instance. Sandbox or Production.
 * This model maps directly to the model from the api.
 */
@javax.persistence.Entity
public class Instance extends Entity {
    public static final String STATUS_OK = "OK";

    @Column(unique = true, nullable = false, length = 255, name = "\"key\"")
    private String key;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String releaseVersion;

    @Column(length = 255)
    private String releaseNumber;

    @Column(length = 255)
    private String status;

    @Enumerated(EnumType.STRING)
    private Environment environment;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.REMOVE)
    private List<Subscription> subscriptions;

    public Instance() {

    }

    public String key() {
        return this.key;
    }

    public Instance setKey(String key) {
        this.key = key;
        return this;
    }

    public String location() {
        return this.location;
    }

    public Instance setLocation(String location) {
        this.location = location;
        return this;
    }

    public String releaseVersion() {
        return this.releaseVersion;
    }

    public Instance setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
        return this;
    }

    public String releaseNumber() {
        return this.releaseNumber;
    }

    public Instance setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
        return this;
    }

    public String status() {
        return this.status;
    }

    public Instance setStatus(String status) {
        this.status = status;
        return this;
    }

    public Environment environment() {
        return this.environment;
    }

    public Instance setEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public List<Subscription> getSubscriptions() {
        return this.subscriptions;
    }

    public Instance setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public enum Environment {
        @JsonProperty("sandbox")
        SANDBOX,

        @JsonProperty("production")
        PRODUCTION
    }

    public DiffResult<Instance> diff(Instance other) {
        return new DiffBuilder<>(other, this, ToStringStyle.DEFAULT_STYLE, false)
            .append("status", other.status, this.status)
            .append("environment", other.environment, this.environment)
            .append("location", other.location, this.location)
            .append("releaseNumber", other.releaseNumber, this.releaseNumber)
            .append("releaseVersion", other.releaseVersion, this.releaseVersion)
            .build();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Instance i) {
            return Objects.equals(this.key, i.key);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }
}
