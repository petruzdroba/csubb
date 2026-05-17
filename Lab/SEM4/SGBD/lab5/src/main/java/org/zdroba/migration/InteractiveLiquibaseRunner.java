package org.zdroba.migration;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class InteractiveLiquibaseRunner {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/SGBD_lab4";
        String user = "student";
        String pass = "student";

        Connection conn = DriverManager.getConnection(url, user, pass);

        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));

        Liquibase liquibase = new Liquibase(
                "db/changelog/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database
        );

        Scanner scanner = new Scanner(System.in);

        String[] changesets = {
                "006-add-phone-column",
                "007-create-projects",
                "008-fk-projects-dept",
                "009-seed-projects",
                "010-alter-salary-type",
                "011-add-index-employee-departament",
                "012-add-index-employees-salary",
                "013-add-version-column-employees",
                "014-soft-delete-employees"
        };

        for (String cs : changesets) {
            System.out.println("\nREADY TO RUN: " + cs);
            System.out.print("Press ENTER to execute...");
            scanner.nextLine();

            liquibase.update(1, new Contexts(), new LabelExpression());
            System.out.println("DONE: " + cs);
        }

        System.out.println("\nALL DONE");
        conn.close();
    }
}
