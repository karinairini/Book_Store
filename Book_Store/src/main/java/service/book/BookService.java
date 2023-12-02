package service.book;

import model.Book;
import model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    Integer getAgeOfBook(Long id);

    Notification<Book> updateBook(Book currentBook, Book wantedBook, String addOrRemove);

    boolean addBook(Book book);
    void removeBook(Book book);
}
