package repository.book;

import model.Book;

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
        books.removeIf(book -> book.getId().equals(id));
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    @Override
    public boolean updateBook(Book book, boolean buy) {
        Optional<Book> optionalBook = findById(book.getId());
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            if (buy) {
                existingBook.setStock(existingBook.getStock() - book.getStock());
            } else {
                existingBook.setAuthor(book.getAuthor());
                existingBook.setTitle(book.getTitle());
                existingBook.setPublishedDate(book.getPublishedDate());
                existingBook.setPrice(book.getPrice());
                existingBook.setStock(book.getStock());
            }
            return true;
        }
        return false;
    }
}
