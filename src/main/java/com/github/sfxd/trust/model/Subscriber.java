package com.github.sfxd.trust.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * Represents a user that has subscribe to notifications
 */
@Entity
public class Subscriber extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    private List<InstanceSubscriber> instanceSubcribers;

    public Subscriber() {

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<InstanceSubscriber> getInstanceSubscribers() {
        return this.instanceSubcribers;
    }

    public void setInstanceSubscribers(List<InstanceSubscriber> instanceSubscribers) {
        this.instanceSubcribers = instanceSubscribers;
    }
}
