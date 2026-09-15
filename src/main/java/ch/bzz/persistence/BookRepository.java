package ch.bzz.persistence;

import ch.bzz.model.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Liest und schreibt Bücher in der Datenbank via Hibernate/JPA.
 */
public class BookRepository {

    private static final Logger log = LoggerFactory.getLogger(BookRepository.class);

    /**
     * @return alle Bücher, nach id sortiert
     */
    public List<Book> findAll() {
        return findAll(0);
    }

    /**
     * Wie {@link #findAll()}, liefert aber höchstens {@code limit} Bücher zurück,
     * sofern {@code limit > 0} ist.
     */
    public List<Book> findAll(int limit) {
        try (EntityManager em = EntityManagerProvider.createEntityManager()) {
            var query = em.createQuery("SELECT b FROM Book b ORDER BY id", Book.class);
            if (limit > 0) {
                query.setMaxResults(limit);
            }
            return query.getResultList();
        }
    }

    /**
     * Speichert alle übergebenen Bücher. Existiert bereits ein Eintrag mit
     * derselben id, wird dieser überschrieben (damit Korrekturen importiert
     * werden können).
     */
    public void saveAll(List<Book> books) {
        log.debug("Speichere {} Bücher", books.size());
        try (EntityManager em = EntityManagerProvider.createEntityManager()) {
            try {
                em.getTransaction().begin();
                books.forEach(em::merge);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Fehler beim Speichern der Bücher", e);
            }
        }
    }
}
