package controller;

import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;
import model.builder.BookBuilder;
import view.CartView;
import view.CustomerView;
import view.EmployeeView;

import java.util.List;
import java.util.stream.Stream;


public class CartController {
    private final CartView cartView;
    private final ComponentFactory componentFactory;
    private final User user;

    public CartController(CartView cartView, ComponentFactory componentFactory, User user) {
        this.cartView = cartView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.cartView.addReturnBookToLibraryButtonListener(new ReturnBookToLibraryButtonListener());
        this.cartView.addContinueShoppingButtonListener(new ContinueShoppingButtonListener());
        this.cartView.addBuyButtonListener(new BuyButtonListener());
        this.cartView.setTotalPriceLabel(String.valueOf(componentFactory.getCartService().totalPriceOfCart(user, componentFactory)));
    }

    private class ReturnBookToLibraryButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Cart cartItemSelected = cartView.getSelectedCartItem();

            componentFactory.getCartService().remove(cartItemSelected.getId());
            cartView.addRecordsToTable();

            Book bookFromCart = componentFactory.getBookService().findById(cartItemSelected.getBookId());
            Book bookFromLibraryUpdated = new BookBuilder().setStock(cartItemSelected.getQuantity()).build();
            componentFactory.getBookService().updateBook(bookFromCart, bookFromLibraryUpdated, "add");

            cartView.setTotalPriceLabel(String.valueOf(componentFactory.getCartService().totalPriceOfCart(user, componentFactory)));
        }
    }

    private class ContinueShoppingButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            cartView.getCartStage().hide();
            if (cartView.getDecision() == 0) {
                CustomerView customerView = new CustomerView(new Stage(), componentFactory);
                customerView.addRecordsToTable();
                CustomerController customerController = new CustomerController(customerView, componentFactory, user);
            } else {
                EmployeeView employeeView = new EmployeeView(new Stage(), componentFactory);
                employeeView.addRecordsToTable();
                EmployeeController employeeController = new EmployeeController(employeeView, componentFactory, user);
            }
        }
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            List<User> userList = componentFactory.getUserRepository().findAll().stream()
                    .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                    .toList();
            for(User user: userList) {
                List<Book> books = componentFactory.getCartService().findAllBooks(user.getId(), componentFactory);
                if(user.getBooksIfEmployee() == null) {
                    user.setBooksIfEmployee(books);
                } else {
                    List<Book> allBooks = Stream.concat(user.getBooksIfEmployee().stream(), books.stream()).toList();
                    user.setBooksIfEmployee(allBooks);
                }
            }
            for(User user1: userList) {
                System.out.println(user1);
            }
            cartView.emptyTable();
            cartView.setTotalPriceLabel("0.0");
        }
    }
}
