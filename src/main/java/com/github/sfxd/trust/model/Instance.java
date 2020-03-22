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

package com.github.sfxd.trust.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

/**
 * Represents an SFDC instance. Sandbox or Production.
 * This model maps directly to the model from the api.
 */
@Entity
public class Instance extends AbstractEntity {
    public static final String STATUS_OK = "OK";

    @Column(nullable = false, length = 255, unique = true)
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
    private List<InstanceSubscriber> instanceSubscribers;

    public Instance() {

    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReleaseVersion() {
        return this.releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getReleaseNumber() {
        return this.releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public List<InstanceSubscriber> getInstanceSubscribers() {
        return this.instanceSubscribers;
    }

    public void setInstanceSubscribers(List<InstanceSubscriber> instanceSubscribers) {
        this.instanceSubscribers = instanceSubscribers;
    }

    public static enum Environment {
        SANDBOX, PRODUCTION
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Instance) {
            var i = (Instance) other;
            return Objects.equals(this.key, i.key);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }
}
