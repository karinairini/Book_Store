package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Cart;
import model.User;
import view.AdministratorView;
import view.ManageUserView;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class AdministratorController {
    private final AdministratorView administratorView;
    private final ComponentFactory componentFactory;
    private final User user;

    public AdministratorController(AdministratorView administratorView, ComponentFactory componentFactory, User user) {
        this.administratorView = administratorView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.administratorView.addManageUsersButtonListener(new ManageUsersButtonListener());
        this.administratorView.addGeneratePDFButtonListener(new GeneratePDFButtonListener());
        this.administratorView.addLogOutButtonListener(new LogOutButtonListener());
    }

    private class ManageUsersButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            List<User> userList = componentFactory.getUserRepository().findAll().stream()
                    .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                    .toList();
            for(User user: userList) {
                System.out.println(user);
            }
            for(User user: userList) {
                List<Book> books = componentFactory.getCartService().findAllBooks(user.getId(), componentFactory);
                for(Book book: books) {
                    System.out.println(book);
                }
            }
            ManageUserView manageUserViewScene = new ManageUserView(new Stage(), componentFactory);
            ManageUserController manageUserController = new ManageUserController(manageUserViewScene, componentFactory);
        }
    }

    private class GeneratePDFButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Long employeeId = administratorView.getEmployeeComboBox().getValue();
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("EmployeeActivity.pdf"));
                document.open();
                Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

                PdfPTable table = new PdfPTable(5);
                addTableHeader(table);
                addRows(table, employeeId);

                document.add(table);
                document.close();
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        private void addTableHeader(PdfPTable table) {
            Stream.of("id", "employee_id", "customer_id", "book_id", "quantity")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });
        }

        private void addRows(PdfPTable table, Long id) {
            List<Cart> cartEmployees = componentFactory.getCartService().findByEmployee(id);
            for(Cart cart: cartEmployees) {
                table.addCell(cart.getId().toString());
                table.addCell(cart.getEmployeeId().toString());
                table.addCell(cart.getUserId().toString());
                table.addCell(cart.getBookId().toString());
                table.addCell(cart.getQuantity().toString());
            }
        }
    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            administratorView.getAdministratorStage().close();
            componentFactory.getLoginView().getLoginStage().show();
        }
    }
}
