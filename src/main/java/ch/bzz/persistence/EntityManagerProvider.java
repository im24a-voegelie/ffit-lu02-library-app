package ch.bzz.persistence;

import ch.bzz.config.Config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Einzige Stelle im Code, an der die JPA-{@link EntityManagerFactory}
 * aufgebaut wird. Die Zugangsdaten stammen aus {@link Config}.
 */
public final class EntityManagerProvider {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("localPU", Config.getProperties());

    private EntityManagerProvider() {
    }

    public static EntityManager createEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}
