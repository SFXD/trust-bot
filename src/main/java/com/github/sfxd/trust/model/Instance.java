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
