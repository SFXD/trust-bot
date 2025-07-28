// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.instances;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.github.sfxd.trust.core.Entity;
import com.github.sfxd.trust.core.users.Subscription;
import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

import static java.util.Objects.requireNonNull;

/// Represents an SFDC instance. Sandbox or Production.
@javax.persistence.Entity
public class Instance extends Entity {
    @Column(unique = true, nullable = false, name = "\"key\"", columnDefinition = "character varying")
    private final String key;

    @Column(columnDefinition = "character varying")
    private String location;

    @Column(columnDefinition = "character varying")
    private String releaseVersion;

    @Column(columnDefinition = "character varying")
    private String releaseNumber;

    @Column(columnDefinition = "character varying")
    private String status;

    @Column(nullable = false)
    private final Environment environment;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.REMOVE)
    private List<Subscription> subscriptions;

    public Instance(String key, Environment environment) {
        this.key = requireNonNull(key);
        this.environment = requireNonNull(environment);
    }

    public String key() {
        return this.key;
    }

    public String location() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String releaseVersion() {
        return this.releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String releaseNumber() {
        return this.releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String status() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Environment environment() {
        return this.environment;
    }

    public List<Subscription> getSubscriptions() {
        return this.subscriptions;
    }

    public enum Environment {
        SANDBOX(1),
        PRODUCTION(2);

        private final int id;
        Environment(int id) {
            this.id = id;
        }

        @DbEnumValue(storage = DbEnumType.INTEGER)
        public int id() {
            return this.id;
        }
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
}
