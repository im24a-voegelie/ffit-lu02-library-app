package ch.bzz.command;

import ch.bzz.model.User;
import ch.bzz.persistence.UserRepository;
import ch.bzz.security.PasswordHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Base64;

/**
 * Legt einen neuen Benutzer an und speichert ihn in der Datenbank.
 * Aufruf: {@code createUser <VORNAME> <NACHNAME> <GEBURTSDATUM> <EMAIL> <PASSWORT>}
 * Das Geburtsdatum wird im Format {@code yyyy-MM-dd} erwartet.
 */
public class CreateUserCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(CreateUserCommand.class);

    private final UserRepository userRepository;

    public CreateUserCommand() {
        this(new UserRepository());
    }

    public CreateUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getName() {
        return "createUser";
    }

    @Override
    public String getDescription() {
        return "Legt einen neuen Benutzer an: createUser <VORNAME> <NACHNAME> <GEBURTSDATUM yyyy-MM-dd> <EMAIL> <PASSWORT>";
    }

    @Override
    public void execute(AppContext context, String argument) {
        String[] parts = (argument == null ? "" : argument.trim()).split("\\s+");
        if (parts.length != 5) {
            log.warn("createUser wurde mit ungültiger Argumentanzahl aufgerufen: '{}'", argument);
            System.out.println("Bitte alle Werte angeben: createUser <VORNAME> <NACHNAME> <GEBURTSDATUM yyyy-MM-dd> <EMAIL> <PASSWORT>");
            return;
        }

        String firstname = parts[0];
        String lastname = parts[1];
        String email = parts[3];
        String password = parts[4];

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.parse(parts[2]);
        } catch (DateTimeParseException e) {
            log.warn("createUser wurde mit ungültigem Geburtsdatum aufgerufen: '{}'", parts[2]);
            System.out.println("Ungültiges Geburtsdatum: '" + parts[2] + "'. Erwartetes Format: yyyy-MM-dd");
            return;
        }

        try {
            byte[] salt = PasswordHandler.generateSalt();
            byte[] hash = PasswordHandler.hashPassword(password, salt);

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            User user = new User(firstname, lastname, dateOfBirth, email, hashBase64, saltBase64);
            userRepository.save(user);

            log.info("Benutzer {} angelegt", email);
            System.out.println("Benutzer angelegt: " + firstname + " " + lastname + " <" + email + ">");
        } catch (NoSuchAlgorithmException e) {
            log.error("Passwort-Hash konnte nicht berechnet werden", e);
            System.out.println("Der Benutzer konnte nicht angelegt werden: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("Benutzer {} konnte nicht gespeichert werden", email, e);
            System.out.println("Der Benutzer konnte nicht gespeichert werden: " + e.getMessage());
        }
    }
}
