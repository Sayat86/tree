package tree.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CentralFactory {
    public static EntityManagerFactory factory;

    public static EntityManagerFactory createManager() {
        factory = Persistence.createEntityManagerFactory("main");
        return factory;
    }

    public static void closeFactory() {
        factory.close();
    }
}
