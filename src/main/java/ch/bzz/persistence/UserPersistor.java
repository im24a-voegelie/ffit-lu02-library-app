package ch.bzz.persistence;

import ch.bzz.model.User;

import jakarta.persistence.EntityManager;

import java.util.Optional;

/**
 * Speichert Benutzer in der Datenbank via Hibernate/JPA.
 */
public class UserPersistor extends AbstractPersistor<User> {

    /**
     * @return der Benutzer mit der angegebenen E-Mailadresse, sofern vorhanden
     */
    public Optional<User> findByEmail(String email) {
        try (EntityManager em = createEntityManager()) {
            var query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst();
        }
    }
}
