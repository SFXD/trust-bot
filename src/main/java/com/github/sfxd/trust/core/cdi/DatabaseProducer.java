// SPDX-License-Identifier: GPL-3.0-or-later
package com.github.sfxd.trust.core.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.ebean.DB;
import io.ebean.Database;

class DatabaseProducer {

    @Produces
    @ApplicationScoped
    Database produceDatabase() {
        return DB.getDefault();
    }
}
