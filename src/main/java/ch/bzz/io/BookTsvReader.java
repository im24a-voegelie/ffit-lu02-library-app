package ch.bzz.io;

import ch.bzz.model.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Liest eine TSV-Datei (Tabulator als Trennzeichen, UTF-8) mit Büchern ein.
 * <p>
 * Erwartetes Format (erste Zeile ist die Kopfzeile und wird übersprungen):
 * <pre>id\tisbn\ttitle\tauthor\tyear</pre>
 */
public class BookTsvReader {

    private static final char DELIMITER = '\t';

    public List<Book> read(Path path) throws IOException {
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine(); // Kopfzeile überspringen

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                books.add(parseLine(line));
            }
        }

        return books;
    }

    private Book parseLine(String line) {
        String[] fields = line.split(String.valueOf(DELIMITER), -1);
        if (fields.length < 4) {
            throw new IllegalArgumentException("Zeile hat zu wenige Spalten: " + line);
        }

        int id = Integer.parseInt(fields[0].trim());
        String isbn = fields[1].trim();
        String title = fields[2].trim();
        String author = fields[3].trim();
        int year = fields.length > 4 && !fields[4].isBlank()
                ? Integer.parseInt(fields[4].trim())
                : 0;

        return new Book(id, isbn, title, author, year);
    }
}
