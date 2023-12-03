package service.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    boolean updateBook(Book book, boolean buy);

    Integer getAgeOfBook(Long id);

    void removeBook(Book book);
}
