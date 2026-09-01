package ch.bzz.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Baut Datenbankverbindungen anhand der Konfiguration in {@code config.properties} auf.
 * Erwartet werden die Schlüssel {@code DB_URL}, {@code DB_USER} und {@code DB_PASSWORD}.
 */
public final class Database {

    private static final String CONFIG_FILE = "config.properties";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        Properties config = loadConfig();
        String url = config.getProperty("DB_URL");
        String user = config.getProperty("DB_USER");
        String password = config.getProperty("DB_PASSWORD");

        if (url == null || url.isBlank()) {
            throw new SQLException("DB_URL ist in " + CONFIG_FILE + " nicht gesetzt");
        }
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadConfig() throws SQLException {
        Properties properties = new Properties();

        Path localFile = Path.of(CONFIG_FILE);
        if (Files.isRegularFile(localFile)) {
            try (InputStream in = Files.newInputStream(localFile)) {
                properties.load(in);
                return properties;
            } catch (IOException e) {
                throw new SQLException("Konnte " + CONFIG_FILE + " nicht lesen", e);
            }
        }

        try (InputStream in = Database.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in != null) {
                properties.load(in);
                return properties;
            }
        } catch (IOException e) {
            throw new SQLException("Konnte " + CONFIG_FILE + " nicht vom Classpath lesen", e);
        }

        throw new SQLException(CONFIG_FILE + " wurde nicht gefunden");
    }
}
