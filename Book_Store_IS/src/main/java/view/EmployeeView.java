package view;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EmployeeView {
    private final Stage employeeStage;
    private final ComponentFactory componentFactory;
    private final GridPane gridPane;
    private TableView<Book> bookTable;
    private Button addBookToLibraryButton;
    private Button addOrderButton;
    private Button viewActivityButton;
    private Button deleteBookFromLibrary;
    private Button updateBookButton;
    private Button logOutButton;
    private TextField bookTitleTextField;
    private TextField bookAuthorTextField;
    private TextField bookPublishedDateTextField;
    private TextField bookPriceTextField;
    private TextField bookStockTextField;
    private ComboBox<Long> clientIdComboBox;
    private Text actionTarget;

    public EmployeeView(Stage primaryStage, ComponentFactory componentFactory) {
        this.employeeStage = primaryStage;
        this.componentFactory = componentFactory;

        primaryStage.setTitle("Hello, Employee!");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 920, 580);
        primaryStage.setScene(scene);

        initializeSceneTitle();

        initializeTable();

        initializeFixedFields();

        initializeVariableFields();

        primaryStage.show();
    }

    private void initializeGridPane() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle() {
        Text sceneTitle = new Text("Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
    }

    private void initializeTable() {
        TableColumn<Book, String> idBook = createTableColumn("ID", 40, "id");
        TableColumn<Book, String> authorBook = createTableColumn("Author", 200, "author");
        TableColumn<Book, String> titleBook = createTableColumn("Title", 200, "title");
        TableColumn<Book, LocalDate> publishedDateBook = createTableColumn("Published Date", 200, "publishedDate");
        TableColumn<Book, Double> priceBook = createTableColumn("Price", 125, "price");
        TableColumn<Book, Integer> stockBook = createTableColumn("Stock", 100, "stock");
        TableColumn<Book, Integer> ageOfBook = createTableColumn("Age", 40, "age");

        bookTable = new TableView<>();
        addRecordsToTable();
        bookTable.getColumns().addAll(Arrays.asList(idBook, authorBook, titleBook, publishedDateBook, priceBook, stockBook, ageOfBook));
        gridPane.add(bookTable, 0, 2, 2, 4);

        bookTable.setOnMouseClicked((MouseEvent event) -> {
            Book selectedBook = getSelectedBook();
            if (selectedBook != null) {
                setTextFieldValues(selectedBook);
            }
        });
    }

    private <S, T> TableColumn<S, T> createTableColumn(String text, double width, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void setTextFieldValues(Book selectedBook) {
        bookTitleTextField.setText(selectedBook.getTitle());
        bookAuthorTextField.setText(selectedBook.getAuthor());
        bookPublishedDateTextField.setText(String.valueOf(selectedBook.getPublishedDate()));
        bookPriceTextField.setText(String.valueOf(selectedBook.getPrice()));
        bookStockTextField.setText(String.valueOf(selectedBook.getStock()));
    }

    private void initializeFixedFields() {
        addBookToLibraryButton = createButton("Add book to library", 1, 11, Pos.BOTTOM_RIGHT);
        addOrderButton = createButton("Order book", 1, 9, Pos.BOTTOM_RIGHT);
        viewActivityButton = createButton("View activity", 1, 0, Pos.BOTTOM_RIGHT);
        deleteBookFromLibrary = createButton("Delete book from library", 1, 12, Pos.BOTTOM_RIGHT);
        updateBookButton = createButton("Update book", 1, 10, Pos.BOTTOM_RIGHT);
        logOutButton = createButton("LogOut", 0, 0, Pos.BOTTOM_LEFT);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 1, 14);
    }

    private Button createButton(String text, int column, int row, Pos alignment) {
        Button button = new Button(text);
        button.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox buttonHBox = new HBox(10);
        buttonHBox.setAlignment(alignment);
        buttonHBox.getChildren().add(button);
        gridPane.add(buttonHBox, column, row);
        return button;
    }

    private void initializeVariableFields() {
        bookTitleTextField = createTextField("Title:", 8, 250);
        bookAuthorTextField = createTextField("Author:", 9, 200);
        bookPriceTextField = createTextField("Price:", 10, 150);
        bookStockTextField = createTextField("Stock/Quantity wanted:", 11, 150);
        bookPublishedDateTextField = createTextField("Published date:", 12, 150);

        Label selectedClientLabel = new Label("Select client:");
        selectedClientLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        clientIdComboBox = new ComboBox<>();
        initializeClientIdComboBox();

        HBox clientIDBox = new HBox(10);
        clientIDBox.getChildren().addAll(selectedClientLabel, clientIdComboBox);

        gridPane.add(clientIDBox, 0, 13);
    }

    private TextField createTextField(String label, int row, double width) {
        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        TextField textField = new TextField();
        textField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        textField.setPrefWidth(width);

        HBox box = new HBox(10);
        box.getChildren().addAll(titleLabel, textField);

        gridPane.add(box, 0, row);

        return textField;
    }

    private void initializeClientIdComboBox() {
        List<Long> clientIDList = componentFactory.getUserService()
                .findAll()
                .stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.CUSTOMER)))
                .map(User::getId)
                .toList();

        ObservableList<Long> observableEmployeeIDList = FXCollections.observableArrayList(clientIDList);
        clientIdComboBox.setItems(observableEmployeeIDList);
        clientIdComboBox.setPrefWidth(100);
    }

    public ObservableList<Book> getBook() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());

        books.forEach(book -> book.setAge(componentFactory.getBookService().getAgeOfBook(book.getId())));
        return books;
    }

    public void addRecordsToTable() {
        bookTable.setItems(getBook());
    }

    public void clearFields() {
        bookTitleTextField.clear();
        bookAuthorTextField.clear();
        bookPublishedDateTextField.clear();
        bookPriceTextField.clear();
        bookStockTextField.clear();
        clientIdComboBox.getSelectionModel().clearSelection();
    }

    public void addBookToLibraryButtonListener(EventHandler<ActionEvent> addBookToLibraryButtonListener) {
        addBookToLibraryButton.setOnAction(addBookToLibraryButtonListener);
    }

    public void addOrderButtonListener(EventHandler<ActionEvent> orderBookButtonListener) {
        addOrderButton.setOnAction(orderBookButtonListener);
    }

    public void addViewActivityButtonListener(EventHandler<ActionEvent> viewActivityButtonListener) {
        viewActivityButton.setOnAction(viewActivityButtonListener);
    }

    public void addDeleteBookFromLibraryButtonListener(EventHandler<ActionEvent> deleteBookFromLibraryButtonListener) {
        deleteBookFromLibrary.setOnAction(deleteBookFromLibraryButtonListener);
    }

    public void addUpdateBookButtonListener(EventHandler<ActionEvent> updateBookButtonListener) {
        updateBookButton.setOnAction(updateBookButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public void setActionTargetText(String text) {
        this.actionTarget.setText(text);
    }

    public TextField getBookTitleTextField() {
        return bookTitleTextField;
    }

    public TextField getBookAuthorTextField() {
        return bookAuthorTextField;
    }

    public TextField getBookPublishedDateTextField() {
        return bookPublishedDateTextField;
    }

    public TextField getBookPriceTextField() {
        return bookPriceTextField;
    }

    public TextField getBookStockTextField() {
        return bookStockTextField;
    }

    public Book getSelectedBook() {
        return bookTable.getSelectionModel().getSelectedItem();
    }

    public ComboBox<Long> getClientIdComboBox() {
        return clientIdComboBox;
    }

    public Stage getEmployeeStage() {
        return employeeStage;
    }
}
