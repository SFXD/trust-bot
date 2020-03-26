package com.github.sfxd.trust.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import io.ebean.DB;
import io.ebean.Database;

class DatabaseProducer {

    @Produces
    @ApplicationScoped
    Database produceDatabase() throws Exception {
        return DB.getDefault();
    }
}
