package com.github.sfxd.trust.inject;

import io.avaje.inject.Bean;
import io.ebean.Database;
import io.ebean.datasource.DataSourceBuilder;
import io.ebean.event.BeanPersistController;
import io.ebean.event.BeanPersistListener;

import java.util.List;
import java.util.Properties;

public class DatabaseProducer {

    @Bean
    public Database produceDatabase(
        Properties properties,
        List<BeanPersistController> controllers,
        List<BeanPersistListener> listeners
    ) {
        return Database.builder()
            .dataSourceBuilder(
                DataSourceBuilder.create()
                    .load(properties)
            )
            .setPersistControllers(controllers)
            .setPersistListeners(listeners)
            .runMigration(true)
            .build();
    }
}
