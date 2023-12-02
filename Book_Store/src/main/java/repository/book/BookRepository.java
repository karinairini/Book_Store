package repository.book;

import model.Book;
import model.validator.Notification;


import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    void remove(Long id);

    void removeAll();

    Notification<Book> updateBook(Book currentBook, Book wantedBook, String addOrRemove);

    Optional<Book> findByTitle(String title);
}
