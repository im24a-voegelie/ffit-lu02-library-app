package ch.bzz.command;

import ch.bzz.io.BookTsvReader;
import ch.bzz.model.Book;
import ch.bzz.persistence.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

/**
 * Liest eine TSV-Datei ein und speichert die enthaltenen Bücher in der Datenbank.
 * Aufruf: {@code importBooks <FILE_PATH>}
 */
public class ImportBooksCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(ImportBooksCommand.class);

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
            log.warn("importBooks wurde ohne Dateipfad aufgerufen");
            System.out.println("Bitte einen Dateipfad angeben: importBooks <FILE_PATH>");
            return;
        }

        Path path = Path.of(argument);

        try {
            List<Book> books = reader.read(path);
            bookRepository.saveAll(books);
            log.info("{} Bücher aus {} importiert", books.size(), path);
            System.out.println(books.size() + " Buch/Bücher aus " + path + " importiert");
        } catch (NoSuchFileException e) {
            log.warn("Importdatei nicht gefunden: {}", path);
            System.out.println("Datei nicht gefunden: " + path);
        } catch (IOException e) {
            log.error("Importdatei konnte nicht gelesen werden: {}", path, e);
            System.out.println("Die Datei konnte nicht gelesen werden: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Importdatei hat ein ungültiges Format: {}", path, e);
            System.out.println("Die Datei hat ein ungültiges Format: " + e.getMessage());
        }
    }
}
