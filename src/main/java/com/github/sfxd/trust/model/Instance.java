package com.github.sfxd.trust.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

/**
 * Represents an SFDC instance. Sandbox or Production.
 */
@Entity
public class Instance extends AbstractEntity {

    @Column(nullable = false, length = 255, unique = true)
    private String key;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String releaseVersion;

    @Column(length = 255)
    private String releaseNumber;

    @Enumerated(EnumType.STRING)
    private Environment environment;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL)
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
}
