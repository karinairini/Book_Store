package service.cart;

import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<Cart> findAll(User user);
    List<Cart> findByEmployee(Long id);

    Optional<Cart> findById(Long id);

    Optional<Cart> findByBookId(Long id);

    boolean save(Cart cartItem);

    Optional<Cart> updateCartQuantity(Optional<Cart> cartItem, Integer quantity);

    void remove(Long id);

    Double totalPriceOfCart(User user, ComponentFactory componentFactory);

    List<Book> findAllBooks(Long employeeId, ComponentFactory componentFactory);
}
