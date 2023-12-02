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
import view.EmployeeView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final ComponentFactory componentFactory;
    private final User user;

    public EmployeeController(EmployeeView employeeView, ComponentFactory componentFactory, User user) {
        this.employeeView = employeeView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.employeeView.addBookToLibraryButtonListener(new AddBookToLibraryButtonListener());
        this.employeeView.addBookToCartButtonListener(new AddBookToCartButtonListener());
        this.employeeView.addViewCartButtonListener(new ViewCartButtonListener());
        this.employeeView.addDeleteBookFromLibraryButtonListener(new DeleteBookFromLibraryButtonListener());
        this.employeeView.addUpdateBookButtonListener(new UpdateBookButtonListener());
        this.employeeView.addLogOutButtonListener(new LogOutButtonListener());
    }

    public Book buildNewBook() {
        String bookAuthor = employeeView.getBookAuthorTextField().getText();
        String bookTitle = employeeView.getBookTitleTextField().getText();
        LocalDate bookPublishedDate = LocalDate.parse(employeeView.getBookPublishedDateTextField().getText());
        Double bookPrice = Double.valueOf(employeeView.getBookPriceTextField().getText());
        Integer bookStock = Integer.valueOf(employeeView.getBookStockTextField().getText());

        Book newBook = new BookBuilder()
                .setAuthor(bookAuthor)
                .setTitle(bookTitle)
                .setPublishedDate(bookPublishedDate)
                .setPrice(bookPrice)
                .setStock(bookStock)
                .build();
        return newBook;
    }

    private class AddBookToLibraryButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (!componentFactory.getBookService().addBook(buildNewBook())) {
                employeeView.setActionTargetText("The book is already in stock! The stock was increased.");
            } else {
                employeeView.setActionTargetText("Added to library successfully!");
            }
            employeeView.addRecordsToTable();
        }
    }

    private class AddBookToCartButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Integer quantityWanted = Integer.valueOf(employeeView.getBookStockTextField().getText());
            Book selectedBook = employeeView.getSelectedBook();
            Long clientSelected = employeeView.getClientIdComboBox().getValue();

            Book wantedBook = new BookBuilder().setStock(quantityWanted).build();

            Notification<Book> addBookToCartNotification = componentFactory.getBookService().updateBook(selectedBook, wantedBook, "remove");

            if (addBookToCartNotification.hasErrors()) {
                employeeView.setActionTargetText(addBookToCartNotification.getFormattedErrors());
            } else {
                Optional<Cart> cartItemCorrespondingToBookSelected = componentFactory.getCartService().findByBookId(selectedBook.getId());

                if (cartItemCorrespondingToBookSelected.isEmpty()) {
                    Cart newCartItem = new CartBuilder()
                            .setUserId(clientSelected)
                            .setEmployeeId(user.getId())
                            .setBookId(addBookToCartNotification.getResult().getId())
                            .setBookTitle(addBookToCartNotification.getResult().getTitle())
                            .setQuantity(quantityWanted)
                            .build();

                    componentFactory.getCartService().save(newCartItem);

                    employeeView.setActionTargetText("Added to cart successfully!");
                } else {
                    componentFactory.getCartService().updateCartQuantity(cartItemCorrespondingToBookSelected, quantityWanted);

                    employeeView.setActionTargetText("Book already in cart! Added more.");
                }
                employeeView.addRecordsToTable();
            }
        }
    }

    private class ViewCartButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            User client = componentFactory.getUserRepository().findById(employeeView.getClientIdComboBox().getValue()).orElse(null);
            if (!componentFactory.getCartService().findAll(client).isEmpty()) {
                employeeView.getEmployeeStage().close();
                CartView cartView = new CartView(new Stage(), componentFactory, 1, client);
                CartController cartController = new CartController(cartView, componentFactory, client);
            } else {
                employeeView.setActionTargetText("Cart is empty!");
            }
            employeeView.addRecordsToTable();
        }
    }

    private class DeleteBookFromLibraryButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            componentFactory.getBookService().removeBook(selectedBook);
            employeeView.setActionTargetText("Deleted from library successfully!");
            employeeView.addRecordsToTable();
        }
    }

    private class UpdateBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            Book wantedBook = buildNewBook();

            componentFactory.getBookService().updateBook(selectedBook, wantedBook, null);

            employeeView.setActionTargetText("Book updated successfully!");
            employeeView.addRecordsToTable();
        }
    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            employeeView.getEmployeeStage().close();
            componentFactory.getLoginView().getLoginStage().show();
        }
    }
}
