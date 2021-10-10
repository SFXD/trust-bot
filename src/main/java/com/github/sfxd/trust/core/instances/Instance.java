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
import com.github.sfxd.trust.core.instanceusers.InstanceUser;

/**
 * Represents an SFDC instance. Sandbox or Production.
 * This model maps directly to the model from the api.
 */
@javax.persistence.Entity
public class Instance extends Entity {
    public static final String STATUS_OK = "OK";

    @Column(unique = true, nullable = false, length = 255)
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
    private List<InstanceUser> instanceUsers;

    public Instance() {

    }

    public String getKey() {
        return this.key;
    }

    public Instance setKey(String key) {
        this.key = key;
        return this;
    }

    public String getLocation() {
        return this.location;
    }

    public Instance setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getReleaseVersion() {
        return this.releaseVersion;
    }

    public Instance setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
        return this;
    }

    public String getReleaseNumber() {
        return this.releaseNumber;
    }

    public Instance setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public Instance setStatus(String status) {
        this.status = status;
        return this;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public Instance setEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public List<InstanceUser> getInstanceUsers() {
        return this.instanceUsers;
    }

    public Instance setInstanceUsers(List<InstanceUser> instanceUsers) {
        this.instanceUsers = instanceUsers;
        return this;
    }

    public enum Environment {
        @JsonProperty("sandbox")
        SANDBOX,

        @JsonProperty("production")
        PRODUCTION
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
