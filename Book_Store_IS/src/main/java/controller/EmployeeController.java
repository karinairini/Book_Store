package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import model.Book;
import model.Command;
import model.User;
import model.builder.BookBuilder;
import model.builder.CommandBuilder;
import view.EmployeeView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final ComponentFactory componentFactory;
    private final User user;

    public EmployeeController(EmployeeView employeeView, ComponentFactory componentFactory, User user) {
        this.employeeView = employeeView;
        this.componentFactory = componentFactory;
        this.user = user;

        this.employeeView.addBookToLibraryButtonListener(new AddBookToLibraryButtonListener());
        this.employeeView.addOrderButtonListener(new AddOrderButtonListener());
        this.employeeView.addViewActivityButtonListener(new ViewActivityButtonListener());
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
        System.out.println(newBook);
        return newBook;
    }

    private class AddBookToLibraryButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (componentFactory.getBookService().save(buildNewBook())) {
                employeeView.setActionTargetText("Added to library successfully!");
            }
            employeeView.addRecordsToTable();
            employeeView.clearFields();
        }
    }

    private class AddOrderButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            Integer quantityWanted = Integer.valueOf(employeeView.getBookStockTextField().getText());
            Integer currentQuantity = selectedBook.getStock();

            if (quantityWanted > currentQuantity) {
                employeeView.setActionTargetText("Not enough books in library!");
            } else {
                selectedBook.setStock(currentQuantity - quantityWanted);
                Long customerSelected = employeeView.getClientIdComboBox().getValue();

                boolean updateSelectedBook = componentFactory.getBookService().updateBook(selectedBook, true);
                if (updateSelectedBook) {
                    Command command = new CommandBuilder()
                            .setCustomerId(customerSelected)
                            .setEmployeeId(user.getId())
                            .setBookId(selectedBook.getId())
                            .setQuantity(quantityWanted)
                            .setPrice(componentFactory.getBookService().findById(selectedBook.getId()).get().getPrice() * quantityWanted)
                            .build();
                    componentFactory.getCommandService().save(command);
                    employeeView.setActionTargetText("Bought book successfully!");
                }
                employeeView.addRecordsToTable();
                employeeView.clearFields();
            }
        }
    }

    private class ViewActivityButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("EmployeeActivity.pdf"));
                document.open();

                PdfPTable table = new PdfPTable(6);
                addTableHeader(table);
                addRows(table);

                document.add(table);
                document.close();
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }
            employeeView.setActionTargetText("Generated PDF successfully!");
        }

        private void addTableHeader(PdfPTable table) throws DocumentException {
            float[] columnWidths = {50f, 80f, 80f, 200f, 70f, 70f};

            table.setTotalWidth(columnWidths);
            table.setLockedWidth(true);

            Stream.of("id", "customer_id", "book_id", "book_title", "quantity", "price")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });
        }

        private void addRows(PdfPTable table) {
            List<Command> commands = componentFactory.getCommandService().findByEmployeeId(user.getId());
            for(Command command: commands) {
                table.addCell(command.getId().toString());
                table.addCell(command.getCustomerId().toString());
                table.addCell(command.getBookId().toString());
                table.addCell(componentFactory.getBookService().findById(command.getBookId()).get().getTitle());
                table.addCell(command.getQuantity().toString());
                table.addCell(String.format("%.2f", command.getPrice()));
            }
        }
    }

    private class DeleteBookFromLibraryButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            componentFactory.getBookService().removeBook(selectedBook);
            employeeView.setActionTargetText("Deleted from library successfully!");
            employeeView.addRecordsToTable();
            employeeView.clearFields();
        }
    }

    private class UpdateBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book selectedBook = employeeView.getSelectedBook();
            selectedBook.setAuthor(employeeView.getBookAuthorTextField().getText());
            selectedBook.setTitle(employeeView.getBookTitleTextField().getText());
            selectedBook.setPrice(Double.valueOf(employeeView.getBookPriceTextField().getText()));
            selectedBook.setStock(Integer.valueOf(employeeView.getBookStockTextField().getText()));
            selectedBook.setPublishedDate(LocalDate.parse(employeeView.getBookPublishedDateTextField().getText()));

            if (componentFactory.getBookService().updateBook(selectedBook, false)) {
                employeeView.setActionTargetText("Book updated successfully!");
            }
            employeeView.addRecordsToTable();
            employeeView.clearFields();
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
