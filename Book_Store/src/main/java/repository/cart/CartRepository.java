package repository.cart;

import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    List<Cart> findAll(Long userId);

    List<Cart> findByEmployee(Long id);

    Optional<Cart> findById(Long id);

    List<Book> findAllBooks(Long employeeId, ComponentFactory componentFactory);

    Optional<Cart> findByBookId(Long id);

    boolean save(Cart cartItem);

    void removeAll();

    Optional<Cart> updateCartQuantity(Optional<Cart> cartItem, Integer quantity);

    void remove(Long id);

}
