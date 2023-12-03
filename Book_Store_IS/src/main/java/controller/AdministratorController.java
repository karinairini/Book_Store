package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Command;
import model.User;
import view.AdministratorView;
import view.ManageUsersView;

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
            ManageUsersView manageUsersViewScene = new ManageUsersView(new Stage(), componentFactory);
            ManageUsersController manageUserController = new ManageUsersController(manageUsersViewScene, componentFactory);
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

                PdfPTable table = new PdfPTable(6);
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
            Stream.of("id", "employee_id", "customer_id", "book_id", "quantity", "price")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });
        }

        private void addRows(PdfPTable table, Long employeeId) {
            List<Command> commands = componentFactory.getCommandService().findByEmployeeId(employeeId);
            for (Command command : commands) {
                table.addCell(command.getId().toString());
                table.addCell(command.getEmployeeId().toString());
                table.addCell(command.getCustomerId().toString());
                table.addCell(command.getBookId().toString());
                table.addCell(command.getQuantity().toString());
                table.addCell(String.format("%.2f", command.getPrice()));
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
