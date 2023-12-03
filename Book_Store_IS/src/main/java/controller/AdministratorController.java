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

    public AdministratorController(AdministratorView administratorView, ComponentFactory componentFactory) {
        this.administratorView = administratorView;
        this.componentFactory = componentFactory;

        this.administratorView.addManageUsersButtonListener(new ManageUsersButtonListener());
        this.administratorView.addViewEmployeeActivityButtonListener(new ViewEmployeeActivityButtonListener());
        this.administratorView.addLogOutButtonListener(new LogOutButtonListener());
    }

    private class ManageUsersButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ManageUsersView manageUsersViewScene = new ManageUsersView(new Stage(), componentFactory);
            ManageUsersController manageUserController = new ManageUsersController(manageUsersViewScene, componentFactory);
        }
    }

    private class ViewEmployeeActivityButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            List<User> users = componentFactory.getUserService().findAll().parallelStream().filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE))).toList();

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("EmployeeActivityAdministrator.pdf"));
                document.open();
                Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
                for(User user: users) {
                    addEmployeeInfo(document, user.getId(), font);

                    PdfPTable table = new PdfPTable(6);
                    addTableHeader(table);
                    addRows(table, user.getId());

                    document.add(table);
                    document.add(Chunk.NEWLINE);
                }
                document.close();
            } catch (DocumentException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            administratorView.setActionTargetText("Generated PDF successfully!");
        }

        private void addEmployeeInfo(Document document, Long employeeId, Font font) throws DocumentException {
            User user = componentFactory.getUserService().findById(employeeId).orElse(null);
            String employeeEmail = user.getUsername();

            document.add(new Paragraph("Employee: ", font));
            document.add(new Paragraph(employeeEmail, font));
            document.add(Chunk.NEWLINE);
        }

        private void addTableHeader(PdfPTable table) throws DocumentException {
            float[] columnWidths = {50f, 80f, 80f, 70f, 70f, 70f};

            table.setTotalWidth(columnWidths);
            table.setLockedWidth(true);

            Stream.of("id", "employee_id", "customer_id", "book_id", "quantity", "price")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(1);
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
