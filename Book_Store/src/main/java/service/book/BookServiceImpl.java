package service.book;

import model.Book;
import model.validator.Notification;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Integer getAgeOfBook(Long id) {
        Book book = this.findById(id).orElse(null);
        LocalDate date = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), date);
    }

    @Override
    public Notification<Book> updateBook(Book currentBook, Book wantedBook, String addOrRemove) {
        return bookRepository.updateBook(currentBook, wantedBook, addOrRemove);
    }

    public boolean addBook(Book book) {
        Optional<Book> correspondingBook = bookRepository.findByTitle(book.getTitle());

        if (!correspondingBook.isEmpty()) {
            bookRepository.updateBook(correspondingBook.get(), book, "add");
            return false;
        } else {
            return bookRepository.save(book);
        }
    }

    @Override
    public void removeBook(Book book) {
        bookRepository.remove(book.getId());
    }
}
