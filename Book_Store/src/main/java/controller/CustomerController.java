package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;
import model.builder.BookBuilder;
import model.builder.CartBuilder;
import model.validator.Notification;
import view.CartView;
import view.CustomerView;


import java.util.Optional;

public class CustomerController {
    private final CustomerView customerView;
    private final ComponentFactory componentFactory;
    private final User user;

    public CustomerController(CustomerView customerView, ComponentFactory componentFactory, User user) {
        this.customerView = customerView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.customerView.addAddToCartButtonListener(new AddToCartButtonListener());
        this.customerView.addViewCartButtonListener(new ViewCartButtonListener());
        this.customerView.addLogOutButtonListener(new LogOutButtonListener());
    }

    private class AddToCartButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Integer quantityWanted = customerView.getQuantitySpinner().getValue();
            Book selectedBook = customerView.getSelectedBook();
            Long employeeSelected = customerView.getEmployeeComboBox().getValue();

            Book wantedBook = new BookBuilder().setStock(quantityWanted).build();
            Notification<Book> addBookToCartNotification = componentFactory.getBookService().updateBook(selectedBook, wantedBook, "remove");

            if (addBookToCartNotification.hasErrors()) {
                customerView.setActionTargetText(addBookToCartNotification.getFormattedErrors());
            } else {
                Optional<Cart> cartItemCorrespondingToBookSelected = componentFactory.getCartService().findByBookId(selectedBook.getId());

                if (cartItemCorrespondingToBookSelected.isEmpty()) {
                    Cart newCartItem = new CartBuilder()
                            .setUserId(user.getId())
                            .setEmployeeId(employeeSelected)
                            .setBookId(addBookToCartNotification.getResult().getId())
                            .setBookTitle(addBookToCartNotification.getResult().getTitle())
                            .setQuantity(quantityWanted)
                            .build();

                    componentFactory.getCartService().save(newCartItem);

                    customerView.setActionTargetText("Added to cart successfully!");
                } else {
                    componentFactory.getCartService().updateCartQuantity(cartItemCorrespondingToBookSelected, quantityWanted);

                    customerView.setActionTargetText("Book already in cart! Added more.");
                }
                customerView.addRecordsToTable();
            }
        }
    }

    private class ViewCartButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (!componentFactory.getCartService().findAll(user).isEmpty()) {
                customerView.getCustomerStage().close();
                CartView cartView = new CartView(new Stage(), componentFactory, 0, user);
                CartController cartController = new CartController(cartView, componentFactory, user);
            } else {
                customerView.setActionTargetText("Cart is empty!");
            }
        }
    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            customerView.getCustomerStage().close();
            componentFactory.getLoginView().getLoginStage().show();
        }
    }
}
