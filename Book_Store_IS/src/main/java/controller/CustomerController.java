package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Command;
import model.User;
import model.builder.BookBuilder;
import model.builder.CommandBuilder;
import model.validator.Notification;
import view.CustomerView;


public class CustomerController {
    private final CustomerView customerView;
    private final ComponentFactory componentFactory;
    private final User user;

    public CustomerController(CustomerView customerView, ComponentFactory componentFactory, User user) {
        this.customerView = customerView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.customerView.addBuyButtonListener(new BuyButtonListener());
        this.customerView.addViewOrdersButtonListener(new ViewOrdersButtonListener());
        this.customerView.addLogOutButtonListener(new LogOutButtonListener());
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = customerView.getSelectedBook();
            Integer quantityWanted = customerView.getQuantitySpinner().getValue();
            Integer currentQuantity = selectedBook.getStock();

            if (quantityWanted > currentQuantity) {
                customerView.setActionTargetText("Not enough books in library!");
            } else {
                selectedBook.setStock(currentQuantity - quantityWanted);
                Long employeeSelected = customerView.getEmployeeComboBox().getValue();

                boolean updateSelectedBook = componentFactory.getBookService().updateBook(selectedBook, true);
                if(updateSelectedBook) {
                    Command command = new CommandBuilder()
                            .setCustomerId(user.getId())
                            .setEmployeeId(employeeSelected)
                            .setBookId(selectedBook.getId())
                            .setQuantity(quantityWanted)
                            .setPrice(componentFactory.getBookService().findById(selectedBook.getId()).get().getPrice() * quantityWanted)
                            .build();
                    componentFactory.getCommandService().save(command);
                    customerView.setActionTargetText("Bought book successfully!");
                }
                customerView.addRecordsToTable();
            }
        }
    }

    private class ViewOrdersButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

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
