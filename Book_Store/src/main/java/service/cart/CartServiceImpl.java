package service.cart;

import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;
import repository.cart.CartRepository;

import java.util.List;
import java.util.Optional;

public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAll(User user) {
        return cartRepository.findAll(user.getId());
    }

    @Override
    public List<Cart> findByEmployee(Long id) {
        return cartRepository.findByEmployee(id);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Optional<Cart> findByBookId(Long id) {
        return cartRepository.findByBookId(id);
    }

    @Override
    public boolean save(Cart cartItem) {
        return cartRepository.save(cartItem);
    }

    @Override
    public Optional<Cart> updateCartQuantity(Optional<Cart> cartItem, Integer quantity) {
        return cartRepository.updateCartQuantity(cartItem, quantity);
    }

    @Override
    public void remove(Long id) {
        cartRepository.remove(id);
    }

    @Override
    public Double totalPriceOfCart(User user, ComponentFactory componentFactory) {
        Double totalPrice = (double) 0;
        List<Cart> cartList = findAll(user);
        for (Cart cart : cartList) {
            totalPrice += cart.getQuantity() * componentFactory.getBookService().findById(cart.getBookId()).getPrice();
        }
        return totalPrice;
    }

    @Override
    public List<Book> findAllBooks(Long employeeId, ComponentFactory componentFactory) {
        return cartRepository.findAllBooks(employeeId, componentFactory);
    }
}
