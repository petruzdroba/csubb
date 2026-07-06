package org.zdroba;

import jakarta.persistence.EntityManager;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {

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

        liquibase.update("");

        System.out.println("Liquibase executed successfully");

        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("Connection works: " + em.isOpen());

        em.close();
        JPAUtil.close();
    }
}