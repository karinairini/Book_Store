package repository.cart;

import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;
import model.builder.CartBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CartRepositoryMySQL implements CartRepository {
    private final Connection connection;

    public CartRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Cart> findAll(Long userId) {
        String sql = "SELECT * FROM cart WHERE user_id = ?;";
        List<Cart> cartItems = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cartItems.add(getCartItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public List<Cart> findByEmployee(Long id) {
        String sql = "SELECT * FROM cart WHERE employee_id = ?;";
        List<Cart> cartItems = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cartItems.add(getCartItemFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        String sql = "SELECT * FROM cart WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Cart cartItem = getCartItemFromResultSet(resultSet);
                return Optional.of(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAllBooks(Long employeeId, ComponentFactory componentFactory) {
        String sql = "SELECT book_title FROM cart WHERE employee_id = ?;";
        List<String> bookTitles = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String bookTitle = resultSet.getString("book_title");
                bookTitles.add(bookTitle);
            }

            for(String bookTitle: bookTitles) {
                Book book = componentFactory.getBookRepository().findByTitle(bookTitle).orElse(null);
                books.add(book);
            }
            return books;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Cart> findByBookId(Long id) {
        String sql = "SELECT * FROM cart WHERE book_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Cart cartItem = getCartItemFromResultSet(resultSet);
                return Optional.of(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Cart cartItem) {
        String sql = "INSERT INTO cart VALUES(null, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, cartItem.getUserId());
            preparedStatement.setLong(2, cartItem.getEmployeeId());
            preparedStatement.setLong(3, cartItem.getBookId());
            preparedStatement.setString(4, cartItem.getBookTitle());
            preparedStatement.setInt(5, cartItem.getQuantity());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM cart WHERE id >= 0;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Cart> updateCartQuantity(Optional<Cart> cartItem, Integer quantity) {
        Cart cartItemNotOptional = cartItem.get();
        try {
            String sql = "UPDATE cart SET quantity = quantity + ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setLong(2, cartItemNotOptional.getId());
            preparedStatement.executeUpdate();

            return this.findById(cartItemNotOptional.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM cart WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cart getCartItemFromResultSet(ResultSet resultSet) throws SQLException {
        return new CartBuilder()
                .setId(resultSet.getLong("id"))
                .setUserId(resultSet.getLong("user_id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setBookId(resultSet.getLong("book_id"))
                .setBookTitle(resultSet.getString("book_title"))
                .setQuantity(resultSet.getInt("quantity"))
                .build();
    }
}
