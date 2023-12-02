package model.builder;

import model.Cart;

public class CartBuilder {
    private Cart cart;

    public CartBuilder() {
        cart = new Cart();
    }

    public CartBuilder setId(Long id) {
        cart.setId(id);
        return this;
    }

    public CartBuilder setUserId(Long userId) {
        cart.setUserId(userId);
        return this;
    }

    public CartBuilder setEmployeeId(Long employeeId) {
        cart.setEmployeeId(employeeId);
        return this;
    }

    public CartBuilder setBookId(Long bookId) {
        cart.setBookId(bookId);
        return this;
    }

    public CartBuilder setBookTitle(String bookTitle) {
        cart.setBookTitle(bookTitle);
        return this;
    }

    public CartBuilder setQuantity(Integer quantity) {
        cart.setQuantity(quantity);
        return this;
    }

    public Cart build() {
        return cart;
    }
}
