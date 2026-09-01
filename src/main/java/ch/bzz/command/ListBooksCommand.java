package ch.bzz.command;

import ch.bzz.model.Book;

/**
 * Listet die vorhandenen Bücher auf, ein Buch pro Zeile.
 */
public class ListBooksCommand implements Command {

    private static final Book BOOK_1 =
            new Book(1, "978-3-8362-9544-4", "Java ist auch eine Insel", "Christian Ullenboom", 2023);
    private static final Book BOOK_2 =
            new Book(2, "978-3-658-43573-8", "Grundkurs Java", "Dietmar Abts", 2024);

    @Override
    public String getName() {
        return "listBooks";
    }

    @Override
    public String getDescription() {
        return "Listet die vorhandenen Bücher auf";
    }

    @Override
    public void execute(AppContext context) {
        for (Book book : new Book[]{BOOK_1, BOOK_2}) {
            System.out.println(book.getTitle());
        }
    }
}
