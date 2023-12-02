package repository.book;

import model.Book;
import model.builder.BookBuilder;
import model.validator.Notification;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";
        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                return Optional.of(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) {
        String sql = "INSERT IGNORE INTO book VALUES(null, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setDouble(4, book.getPrice());
            preparedStatement.setInt(5, book.getStock());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM book WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Notification<Book> updateBook(Book currentBook, Book wantedBook, String addOrRemove) {
        Notification<Book> updateBookNotification = new Notification<>();

        String sqlDefault = "UPDATE book SET author = ?, title = ?, publishedDate = ?, price = ? WHERE id = ?;";
        String sqlAdd = "UPDATE book SET stock = ? WHERE id = ?";
        String sqlRemove = "UPDATE book SET stock = stock - ? WHERE id = ?";
        try {
            Integer currentBookQuantity = currentBook.getStock();
            Integer quantityWanted = wantedBook.getStock();
            if (quantityWanted > currentBookQuantity && addOrRemove.equals("remove")) {
                updateBookNotification.addError("Not enough books in library!");
                return updateBookNotification;
            } else {
                String sql = "";

                if(addOrRemove == null) {
                    sql = sqlDefault;
                    String bookTitle = wantedBook.getTitle();
                    String bookAuthor = wantedBook.getAuthor();
                    LocalDate localDate = wantedBook.getPublishedDate();
                    Double bookPrice = wantedBook.getPrice();

                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, bookAuthor);
                    preparedStatement.setString(2, bookTitle);
                    preparedStatement.setDate(3, java.sql.Date.valueOf(localDate));
                    preparedStatement.setDouble(4, bookPrice);
                    preparedStatement.setLong(5, currentBook.getId());
                    preparedStatement.executeUpdate();

                    updateBookNotification.setResult(findById(currentBook.getId()).orElse(null));
                } else {
                    if (addOrRemove.equals("add")) {
                        sql = sqlAdd;
                        quantityWanted += currentBookQuantity;
                    } else if (addOrRemove.equals("remove")) {
                        sql = sqlRemove;
                    }
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, quantityWanted);
                    preparedStatement.setLong(2, currentBook.getId());
                    preparedStatement.executeUpdate();

                    currentBook.setStock(quantityWanted);
                    updateBookNotification.setResult(currentBook);
                }
                return updateBookNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            updateBookNotification.addError("Something is wrong with the Database!");
        }
        return updateBookNotification;
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        String sql = "SELECT * FROM book WHERE title = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                return Optional.of(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setPrice(resultSet.getDouble("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}
