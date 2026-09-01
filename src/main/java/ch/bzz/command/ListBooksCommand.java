package ch.bzz.command;

import ch.bzz.model.Book;
import ch.bzz.persistence.BookRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Listet die in der Datenbank vorhandenen Bücher auf, ein Buch pro Zeile.
 */
public class ListBooksCommand implements Command {

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
        return "Listet die vorhandenen Bücher auf";
    }

    @Override
    public void execute(AppContext context, String argument) {
        try {
            List<Book> books = bookRepository.findAll();
            for (Book book : books) {
                System.out.println(book.getTitle());
            }
        } catch (SQLException e) {
            System.out.println("Die Bücher konnten nicht geladen werden: " + e.getMessage());
        }
    }
}
