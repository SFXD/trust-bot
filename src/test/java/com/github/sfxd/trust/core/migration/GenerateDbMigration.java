package com.github.sfxd.trust.core.migration;

import java.io.IOException;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

class GenerateDbMigration {

    public static void main(String[] args) throws IOException {
        System.setProperty("ddl.migration.version", "1.0");
        System.setProperty("ddl.migration.name", "init");

        var migration = DbMigration.create();
        migration.setPlatform(Platform.H2);

        migration.generateMigration();
    }
}
