package ch.bzz.command;

import ch.bzz.io.BookTsvReader;
import ch.bzz.model.Book;
import ch.bzz.persistence.BookRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

/**
 * Liest eine TSV-Datei ein und speichert die enthaltenen Bücher in der Datenbank.
 * Aufruf: {@code importBooks <FILE_PATH>}
 */
public class ImportBooksCommand implements Command {

    private final BookTsvReader reader;
    private final BookRepository bookRepository;

    public ImportBooksCommand() {
        this(new BookTsvReader(), new BookRepository());
    }

    public ImportBooksCommand(BookTsvReader reader, BookRepository bookRepository) {
        this.reader = reader;
        this.bookRepository = bookRepository;
    }

    @Override
    public String getName() {
        return "importBooks";
    }

    @Override
    public String getDescription() {
        return "Importiert Bücher aus einer TSV-Datei in die Datenbank: importBooks <FILE_PATH>";
    }

    @Override
    public void execute(AppContext context, String argument) {
        if (argument == null || argument.isBlank()) {
            System.out.println("Bitte einen Dateipfad angeben: importBooks <FILE_PATH>");
            return;
        }

        Path path = Path.of(argument);

        try {
            List<Book> books = reader.read(path);
            bookRepository.saveAll(books);
            System.out.println(books.size() + " Buch/Bücher aus " + path + " importiert");
        } catch (IOException e) {
            System.out.println("Die Datei konnte nicht gelesen werden: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Die Bücher konnten nicht gespeichert werden: " + e.getMessage());
        }
    }
}
