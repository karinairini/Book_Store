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
    private Stage employeeStage;
    private ComponentFactory componentFactory;
    private GridPane gridPane;
    private TableView<Book> bookTable;
    private Button addBookToLibraryButton;
    private Button addOrderButton;
    private Button viewOrdersButton;
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
        TableColumn<Book, String> idBook = new TableColumn<>("ID");
        idBook.setMinWidth(40);
        idBook.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> authorBook = new TableColumn<>("Author");
        authorBook.setMinWidth(200);
        authorBook.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> titleBook = new TableColumn<>("Title");
        titleBook.setMinWidth(200);
        titleBook.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, LocalDate> publishedDateBook = new TableColumn<>("Published Date");
        publishedDateBook.setMinWidth(200);
        publishedDateBook.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<Book, Double> priceBook = new TableColumn<>("Price");
        priceBook.setMinWidth(125);
        priceBook.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, Integer> stockBook = new TableColumn<>("Stock");
        stockBook.setMinWidth(100);
        stockBook.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Book, Integer> ageOfBook = new TableColumn<>("Age");
        ageOfBook.setMinWidth(40);
        ageOfBook.setCellValueFactory(new PropertyValueFactory<>("age"));

        bookTable = new TableView<>();
        addRecordsToTable();
        bookTable.getColumns().addAll(Arrays.asList(idBook, authorBook, titleBook, publishedDateBook, priceBook, stockBook, ageOfBook));
        gridPane.add(bookTable, 0, 2, 2, 4);

        bookTable.setOnMouseClicked((MouseEvent event) -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                bookTitleTextField.setText(selectedBook.getTitle());
                bookAuthorTextField.setText(selectedBook.getAuthor());
                bookPublishedDateTextField.setText(String.valueOf(selectedBook.getPublishedDate()));
                bookPriceTextField.setText(String.valueOf(selectedBook.getPrice()));
                bookStockTextField.setText(String.valueOf(selectedBook.getStock()));
            }
        });
    }

    private void initializeFixedFields() {
        addBookToLibraryButton = new Button("Add book to library");
        addBookToLibraryButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox addBookToLibraryButtonHBox = new HBox(10);
        addBookToLibraryButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        addBookToLibraryButtonHBox.getChildren().add(addBookToLibraryButton);
        gridPane.add(addBookToLibraryButtonHBox, 1, 11);

        addOrderButton = new Button("Order book");
        addOrderButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox addBookToCartButtonHBox = new HBox(10);
        addBookToCartButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        addBookToCartButtonHBox.getChildren().add(addOrderButton);
        gridPane.add(addBookToCartButtonHBox, 1, 9);

        viewOrdersButton = new Button("View orders");
        viewOrdersButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(viewOrdersButton, 1, 0);
        GridPane.setHalignment(viewOrdersButton, HPos.RIGHT);

        deleteBookFromLibrary = new Button("Delete book from library");
        deleteBookFromLibrary.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox deleteBookFromLibraryButtonHBox = new HBox(10);
        deleteBookFromLibraryButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        deleteBookFromLibraryButtonHBox.getChildren().add(deleteBookFromLibrary);
        gridPane.add(deleteBookFromLibraryButtonHBox, 1, 12);

        updateBookButton = new Button("Update book");
        updateBookButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox updateBookButtonHbox = new HBox(10);
        updateBookButtonHbox.setAlignment(Pos.BOTTOM_RIGHT);
        updateBookButtonHbox.getChildren().add(updateBookButton);
        gridPane.add(updateBookButtonHbox, 1, 10);

        logOutButton = new Button("LogOut");
        logOutButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(logOutButton, 0, 0);
        GridPane.setHalignment(logOutButton, HPos.LEFT);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 1, 14);
    }

    private void initializeVariableFields() {
        Label bookTitleLabel = new Label("Title:");
        bookTitleLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        bookTitleTextField = new TextField();
        bookTitleTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        bookTitleTextField.setPrefWidth(250);

        HBox bookTitleBox = new HBox(10);
        bookTitleBox.getChildren().addAll(bookTitleLabel, bookTitleTextField);

        gridPane.add(bookTitleBox, 0, 8);

        Label bookAuthorLabel = new Label("Author:");
        bookAuthorLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        bookAuthorTextField = new TextField();
        bookAuthorTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        bookAuthorTextField.setPrefWidth(200);

        HBox bookAuthorBox = new HBox(10);
        bookAuthorBox.getChildren().addAll(bookAuthorLabel, bookAuthorTextField);

        gridPane.add(bookAuthorBox, 0, 9);

        Label bookPriceLabel = new Label("Price:");
        bookPriceLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        bookPriceTextField = new TextField();
        bookPriceTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        bookPriceTextField.setPrefWidth(150);

        HBox bookPriceBox = new HBox(10);
        bookPriceBox.getChildren().addAll(bookPriceLabel, bookPriceTextField);

        gridPane.add(bookPriceBox, 0, 10);

        Label bookStockLabel = new Label("Stock/Quantity wanted:");
        bookStockLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        bookStockTextField = new TextField();
        bookStockTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        bookStockTextField.setPrefWidth(150);

        HBox bookStockBox = new HBox(10);
        bookStockBox.getChildren().addAll(bookStockLabel, bookStockTextField);

        gridPane.add(bookStockBox, 0, 11);

        Label bookPublishedDateLabel = new Label("Published date:");
        bookPublishedDateLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        bookPublishedDateTextField = new TextField();
        bookPublishedDateTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        bookPublishedDateTextField.setMaxWidth(150);

        HBox bookPublishedDateBox = new HBox(10);
        bookPublishedDateBox.getChildren().addAll(bookPublishedDateLabel, bookPublishedDateTextField);

        gridPane.add(bookPublishedDateBox, 0, 12);

        Label selectedClientLabel = new Label("Select client:");
        selectedClientLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        clientIdComboBox = new ComboBox<>();
        List<Long> clientIDList = componentFactory.getUserRepository()
                .findAll()
                .stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.CUSTOMER)))
                .map(User::getId)
                .toList();

        ObservableList<Long> observableEmployeeIDList = FXCollections.observableArrayList(clientIDList);
        clientIdComboBox.setItems(observableEmployeeIDList);
        clientIdComboBox.setPrefWidth(100);

        HBox clientIDBox = new HBox(10);
        clientIDBox.getChildren().addAll(selectedClientLabel, clientIdComboBox);

        gridPane.add(clientIDBox, 0, 13);
    }

    public ObservableList<Book> getBook() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());

        books.forEach(book -> {
            book.setAge(componentFactory.getBookService().getAgeOfBook(book.getId()));
        });
        return books;
    }

    public void addRecordsToTable() {
        bookTable.setItems(getBook());
    }

    public void addBookToLibraryButtonListener(EventHandler<ActionEvent> addBookToLibraryButtonListener) {
        addBookToLibraryButton.setOnAction(addBookToLibraryButtonListener);
    }

    public void addOrderButtonListener(EventHandler<ActionEvent> orderBookButtonListener) {
        addOrderButton.setOnAction(orderBookButtonListener);
    }

    public void addViewOrdersButtonListener(EventHandler<ActionEvent> viewOrdersButtonListener) {
        viewOrdersButton.setOnAction(viewOrdersButtonListener);
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
