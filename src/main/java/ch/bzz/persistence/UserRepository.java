package ch.bzz.persistence;

import ch.bzz.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;

/**
 * Speichert Benutzer in der Datenbank.
 */
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public void save(User user) {
        log.debug("Speichere Benutzer {}", user.getEmail());
        try (EntityManager entityManager = EntityManagerProvider.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }
    }
}
