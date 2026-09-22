package ch.bzz.web;

import ch.bzz.model.User;
import ch.bzz.persistence.UserPersistor;
import ch.bzz.security.JwtHandler;
import ch.bzz.security.PasswordHandler;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

/**
 * Startet die REST-API der Bibliotheksapplikation.
 */
public class JavalinApp {

    private static final Logger log = LoggerFactory.getLogger(JavalinApp.class);
    private static final int PORT = 7070;

    public static void main(String[] args) {
        createApp().start(PORT);
    }

    public static Javalin createApp() {
        UserPersistor userPersistor = new UserPersistor();

        Javalin app = Javalin.create();
        app.post("/auth/login", ctx -> login(ctx, userPersistor));
        app.events(event -> event.serverStopping(userPersistor::close));

        return app;
    }

    private static void login(Context ctx, UserPersistor userPersistor) {
        var json = ctx.bodyValidator(Map.class)
                .check(m -> m.containsKey("email"), "email is required")
                .check(m -> m.containsKey("password"), "password is required")
                .get();

        String inputEmail = (String) json.get("email");
        String inputPassword = (String) json.get("password");

        Optional<User> maybeUser = userPersistor.findByEmail(inputEmail);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            byte[] storedSalt = Base64.getDecoder().decode(user.getPasswordSalt());
            byte[] storedHash = Base64.getDecoder().decode(user.getPasswordHash());

            try {
                if (PasswordHandler.verifyPassword(inputPassword, storedHash, storedSalt)) {
                    String jwt = JwtHandler.createJwt(inputEmail, user.getId());
                    ctx.json(Map.of("token", jwt));
                    return;
                }
            } catch (NoSuchAlgorithmException e) {
                log.error("Passwort konnte nicht überprüft werden", e);
            }
        }

        // Use the same error message if the user is not found and if the password is wrong
        ctx.status(401).json(Map.of("error", "Invalid email or password"));
    }
}
