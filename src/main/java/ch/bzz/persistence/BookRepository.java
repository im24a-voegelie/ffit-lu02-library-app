package ch.bzz.persistence;

import ch.bzz.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Liest und schreibt Bücher in der Datenbank.
 */
public class BookRepository {

    private static final String SELECT_ALL =
            "SELECT id, isbn, title, author, publication_year FROM books ORDER BY id";

    private static final String UPSERT =
            "INSERT INTO books (id, isbn, title, author, publication_year) "
                    + "VALUES (?, ?, ?, ?, ?) "
                    + "ON CONFLICT (id) DO UPDATE SET "
                    + "isbn = EXCLUDED.isbn, "
                    + "title = EXCLUDED.title, "
                    + "author = EXCLUDED.author, "
                    + "publication_year = EXCLUDED.publication_year";

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

    /**
     * Speichert alle übergebenen Bücher. Existiert bereits ein Eintrag mit
     * derselben id, wird dieser überschrieben (damit Korrekturen importiert
     * werden können).
     */
    public void saveAll(List<Book> books) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPSERT)) {

            for (Book book : books) {
                statement.setInt(1, book.getId());
                statement.setString(2, book.getIsbn());
                statement.setString(3, book.getTitle());
                statement.setString(4, book.getAuthor());
                statement.setInt(5, book.getYear());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
