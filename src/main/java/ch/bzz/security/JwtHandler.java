package ch.bzz.security;

import ch.bzz.config.Config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Erzeugt signierte JWTs zur Authentifizierung von Benutzern.
 */
public final class JwtHandler {

    private static final long EXPIRATION_MILLIS = 3_600_000;
    private static final SecretKey JWT_KEY =
            Keys.hmacShaKeyFor(Config.get("jwt.secret").getBytes(StandardCharsets.UTF_8));

    private JwtHandler() {
    }

    public static String createJwt(String subject, Integer userId) {
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + EXPIRATION_MILLIS);

        return Jwts.builder()
                .subject(subject)
                .claim("userId", userId)
                .issuedAt(currentTime)
                .expiration(expirationTime)
                .signWith(JWT_KEY)
                .compact();
    }
}
