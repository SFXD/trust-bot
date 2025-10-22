// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * The base representation of all entities.
 */
@MappedSuperclass
public abstract class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Entity() {

    }

    public Entity(Long id) {
        this.id = id;
    }

    public Long id() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }
}
