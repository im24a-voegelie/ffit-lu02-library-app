package ch.bzz.persistence;

import ch.bzz.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

/**
 * Basisklasse für alle Persistor-Klassen. Kapselt die eigene
 * {@link EntityManagerFactory} und das Transaktionshandling; muss nach
 * Gebrauch via {@link #close()} (z.B. in einem try-with-resources) geschlossen werden.
 *
 * @param <T> der Entitätstyp, den dieser Persistor verwaltet
 */
public abstract class AbstractPersistor<T> implements AutoCloseable {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private final EntityManagerFactory emf;

    protected AbstractPersistor() {
        this.emf = Persistence.createEntityManagerFactory("localPU", Config.getProperties());
    }

    public void save(T t) {
        executeTransaction(em -> em.merge(t));
    }

    protected void executeTransaction(Consumer<EntityManager> action) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                action.accept(em);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Error during transaction:", e);
                throw e;
            }
        }
    }

    protected EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            log.info("EntityManagerFactory closed");
        }
    }
}
