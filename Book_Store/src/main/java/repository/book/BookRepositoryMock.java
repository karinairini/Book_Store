package repository.book;

import model.Book;
import model.validator.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {
    private List<Book> books;

    public BookRepositoryMock() {
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        //creeaza thread-uri separate
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public Notification<Book> updateBook(Book currentBook, Book wantedBook, String addOrRemove) {
        return null;
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return books.parallelStream()
                .filter(it -> it.getTitle().equals(title))
                .findFirst();
    }
}
