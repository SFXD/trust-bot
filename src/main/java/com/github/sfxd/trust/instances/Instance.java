// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.instances;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.sfxd.trust.events.Event;
import com.github.sfxd.trust.events.Eventful;
import io.ebean.annotation.History;
import jakarta.persistence.*;

import com.github.sfxd.trust.Entity;
import com.github.sfxd.trust.users.Subscription;
import com.github.sfxd.trust.util.Diff;
import com.github.sfxd.trust.util.DiffBuilder;
import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/// Represents an SFDC instance. Sandbox or Production.
@jakarta.persistence.Entity
@History
public class Instance extends Entity implements Eventful {
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

    @Transient
    private final List<Event> events = new ArrayList<>();

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
        if (!this.status.equals(status)) {
            this.events.add(new InstanceStatusChangeEvent(this));
        }

        this.status = status;
    }

    public Environment environment() {
        return this.environment;
    }

    public List<Subscription> subscriptions() {
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

    public Collection<Event> events() {
        return unmodifiableList(this.events);
    }

    public List<Diff<?>> diff(Instance other) {
        return new DiffBuilder()
            .append("status", other.status, this.status)
            .append("environment", other.environment, this.environment)
            .append("location", other.location, this.location)
            .append("releaseNumber", other.releaseNumber, this.releaseNumber)
            .append("releaseVersion", other.releaseVersion, this.releaseVersion)
            .build();
    }
}
