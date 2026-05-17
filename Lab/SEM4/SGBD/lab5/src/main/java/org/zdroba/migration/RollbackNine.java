package org.zdroba.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RollbackNine {

    public static void main(String[] args) throws SQLException, LiquibaseException {

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SGBD_lab4",
                "student",
                "student"
        );

        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase(
                "db/changelog/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database
        );

        for(int idx=0; idx<9;idx++)
            liquibase.rollback(1, "");

        System.out.println("Rolled back last changeSet");
    }
}