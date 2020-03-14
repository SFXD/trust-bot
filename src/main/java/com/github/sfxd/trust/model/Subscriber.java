package com.github.sfxd.trust.model;

import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE)
    private List<InstanceSubscriber> instanceSubcribers;

    public Subscriber(String username) {
        this.username = username;
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof Subscriber) {
            var subscriber = (Subscriber) other;
            return Objects.equals(this.username, subscriber.username);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }
}
