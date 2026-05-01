package org.zdroba;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static final EntityManagerFactory emf;

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", String.format(
                "jdbc:mysql://%s:%s/%s",
                dotenv.get("DB_HOST", "localhost"),
                dotenv.get("DB_PORT", "3306"),
                dotenv.get("DB_NAME", "SGBD_lab4")
        ));
        props.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER", "student"));
        props.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD", "student"));

        emf = Persistence.createEntityManagerFactory("default", props);
    }

    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    public static void close(){
        emf.close();
    }
}
