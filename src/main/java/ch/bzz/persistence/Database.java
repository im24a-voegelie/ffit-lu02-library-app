package ch.bzz.persistence;

import ch.bzz.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Einzige Stelle im Code, an der eine Datenbankverbindung aufgebaut wird.
 * Die Zugangsdaten stammen aus {@link Config} ({@code config.properties}).
 */
public final class Database {

    private static final String URL = Config.get("DB_URL");
    private static final String USER = Config.get("DB_USER");
    private static final String PASSWORD = Config.get("DB_PASSWORD");

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
