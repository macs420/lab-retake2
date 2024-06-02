import java.util.List;

public interface BookOperations {
    void addBook(Book book);
    void removeBook(Book book);
    void updateBook(Book oldBook, Book newBook);
    List<Book> getBooks();
}
