package ch.bzz.persistence;

import ch.bzz.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Liest Bücher aus der Datenbank.
 */
public class BookRepository {

    private static final String SELECT_ALL =
            "SELECT id, isbn, title, author, publication_year FROM books ORDER BY id";

    /**
     * Baut eine Datenbankverbindung auf, liest alle Bücher und erstellt pro
     * Datensatz ein {@link Book}-Objekt.
     *
     * @return Liste aller Bücher, nach id sortiert
     */
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("publication_year")));
            }
        }

        return books;
    }
}
