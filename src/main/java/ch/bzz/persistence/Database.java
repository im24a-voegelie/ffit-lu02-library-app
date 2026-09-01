package ch.bzz.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Baut Datenbankverbindungen anhand der Konfiguration in {@code config.properties} auf.
 * <p>
 * Die Datei liegt im Wurzelverzeichnis des Projekts, wird nicht eingecheckt und
 * enthält die Schlüssel {@code DB_URL}, {@code DB_USER} und {@code DB_PASSWORD}.
 */
public final class Database {

    private static final String CONFIG_FILE = "config.properties";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        Properties config = loadConfig();
        return DriverManager.getConnection(
                config.getProperty("DB_URL"),
                config.getProperty("DB_USER"),
                config.getProperty("DB_PASSWORD"));
    }

    private static Properties loadConfig() throws SQLException {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            throw new SQLException(CONFIG_FILE + " konnte nicht gelesen werden", e);
        }
        return properties;
    }
}
