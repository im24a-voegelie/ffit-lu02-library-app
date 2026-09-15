package ch.bzz.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Zentraler Zugriff auf die Konfigurationswerte aus {@code config.properties}
 * im Wurzelverzeichnis des Projekts. Die Datei wird einmalig geladen.
 */
public final class Config {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private Config() {
    }

    /**
     * Liefert den Wert zum angegebenen Schlüssel.
     *
     * @throws IllegalStateException wenn der Schlüssel nicht gesetzt ist
     */
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Konfiguration '" + key + "' fehlt in " + CONFIG_FILE);
        }
        return value;
    }

    /**
     * Liefert alle Konfigurationswerte, u.a. die {@code jakarta.persistence.jdbc.*}
     * Properties, um damit eine {@link jakarta.persistence.EntityManagerFactory} zu erzeugen.
     */
    public static Properties getProperties() {
        return PROPERTIES;
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(
                    CONFIG_FILE + " konnte nicht gelesen werden", e);
        }
        return properties;
    }
}
