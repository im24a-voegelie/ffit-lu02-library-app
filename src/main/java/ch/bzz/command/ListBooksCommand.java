package ch.bzz.command;

import ch.bzz.model.Book;
import ch.bzz.persistence.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Listet die in der Datenbank vorhandenen Bücher auf, ein Buch pro Zeile.
 * Optional kann ein Limit angegeben werden: {@code listBooks 10}.
 */
public class ListBooksCommand implements Command {

    private static final Logger log = LoggerFactory.getLogger(ListBooksCommand.class);

    private final BookRepository bookRepository;

    public ListBooksCommand() {
        this(new BookRepository());
    }

    public ListBooksCommand(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public String getName() {
        return "listBooks";
    }

    @Override
    public String getDescription() {
        return "Listet die vorhandenen Bücher auf (optional: listBooks <LIMIT>)";
    }

    @Override
    public void execute(AppContext context, String argument) {
        Integer limit = parseLimit(argument);
        if (argument != null && !argument.isBlank() && limit == null) {
            // Ungültiges Argument wurde bereits geloggt; Applikation läuft weiter.
            return;
        }

        try {
            List<Book> books = (limit == null)
                    ? bookRepository.findAll()
                    : bookRepository.findAll(limit);
            for (Book book : books) {
                System.out.println(book.getTitle());
            }
        } catch (RuntimeException e) {
            log.error("Bücher konnten nicht aus der Datenbank geladen werden", e);
            System.out.println("Die Bücher konnten nicht geladen werden: " + e.getMessage());
        }
    }

    /**
     * @return das Limit, {@code null} wenn kein oder ein ungültiges Argument angegeben wurde
     */
    private Integer parseLimit(String argument) {
        if (argument == null || argument.isBlank()) {
            return null;
        }
        try {
            int limit = Integer.parseInt(argument.trim());
            if (limit < 0) {
                log.warn("Negatives Limit für listBooks: {}", limit);
                System.out.println("Das Limit darf nicht negativ sein: " + argument);
                return null;
            }
            return limit;
        } catch (NumberFormatException e) {
            log.warn("Ungültiges Limit für listBooks: '{}'", argument);
            System.out.println("Ungültige Zahl: '" + argument + "'. Bitte eine ganze Zahl angeben.");
            return null;
        }
    }
}
