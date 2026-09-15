package ch.bzz.persistence;

import ch.bzz.model.Book;

import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Liest und schreibt Bücher in der Datenbank via Hibernate/JPA.
 */
public class BookPersistor extends AbstractPersistor<Book> {

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
        try (EntityManager em = createEntityManager()) {
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
        executeTransaction(em -> books.forEach(em::merge));
    }
}
