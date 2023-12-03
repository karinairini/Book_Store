package view;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class CustomerView {
    private final Stage customerStage;
    private final ComponentFactory componentFactory;
    private final GridPane gridPane;
    private TableView<Book> bookTable;
    private TextField selectedBookTextField;
    private Spinner<Integer> quantitySpinner;
    private ComboBox<Long> employeeComboBox;
    private Button buyBookButton;
    private Button logOutButton;
    private Text actionTarget;

    public CustomerView(Stage primaryStage, ComponentFactory componentFactory) {
        this.customerStage = primaryStage;
        this.componentFactory = componentFactory;

        primaryStage.setTitle("Hello, Customer!");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 920, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle();

        initializeTable();

        initializeVariableFields();

        initializeFixedFields();

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
        gridPane.add(bookTable, 0, 2, 2, 1);

        bookTable.setOnMouseClicked((MouseEvent event) -> {
            Book selectedBook = getSelectedBook();
            if (selectedBook != null) {
                selectedBookTextField.setText(selectedBook.getTitle());
            }
        });
    }

    private <S, T> TableColumn<S, T> createTableColumn(String text, double width, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private ObservableList<Book> getBook() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());
        books.removeIf(book -> book.getStock() == 0);

        books.forEach(book -> book.setAge(componentFactory.getBookService().getAgeOfBook(book.getId())));
        return books;
    }

    public void addRecordsToTable() {
        bookTable.setItems(getBook());
    }

    private void initializeVariableFields() {
        selectedBookTextField = createTextField(Font.font("Tahome", FontWeight.NORMAL, 14), Font.font("Tahome", FontWeight.NORMAL, 12));
        quantitySpinner = createSpinner(Font.font("Tahome", FontWeight.NORMAL, 14), Font.font("Tahome", FontWeight.NORMAL, 12));
        employeeComboBox = createComboBox(Font.font("Tahome", FontWeight.NORMAL, 14), getEmployeeIDList());
    }

    private TextField createTextField(Font labelFont, Font textFieldFont) {
        Label titleLabel = new Label("Book Title:");
        titleLabel.setFont(labelFont);

        TextField textField = new TextField();
        textField.setFont(textFieldFont);
        textField.setPrefWidth(200);

        HBox box = createHBoxWithSpacing(titleLabel, textField);
        gridPane.add(box, 0, 4);

        return textField;
    }

    private Spinner<Integer> createSpinner(Font labelFont, Font spinnerFont) {
        Label titleLabel = new Label("Select Quantity:");
        titleLabel.setFont(labelFont);

        Spinner<Integer> spinner = new Spinner<>(1, 100, 1);
        spinner.setEditable(true);
        spinner.setPrefWidth(120);
        spinner.getEditor().setFont(spinnerFont);

        HBox box = createHBoxWithSpacing(titleLabel, spinner);
        gridPane.add(box, 0, 5);

        return spinner;
    }

    private ComboBox<Long> createComboBox(Font labelFont, List<Long> items) {
        Label titleLabel = new Label("Select Employee:");
        titleLabel.setFont(labelFont);

        ComboBox<Long> comboBox = new ComboBox<>();
        ObservableList<Long> observableList = FXCollections.observableArrayList(items);
        comboBox.setItems(observableList);
        comboBox.setPrefWidth(100);

        HBox box = createHBoxWithSpacing(titleLabel, comboBox);
        gridPane.add(box, 0, 6);

        return comboBox;
    }

    private List<Long> getEmployeeIDList() {
        return componentFactory.getUserService()
                .findAll()
                .stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                .map(User::getId)
                .toList();
    }

    private HBox createHBoxWithSpacing(Node... nodes) {
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(nodes);
        return hBox;
    }

    private void initializeFixedFields() {
        buyBookButton = createButton("Buy Book", 1, 6, Pos.BOTTOM_RIGHT);
        logOutButton = createButton("LogOut", 0, 0, Pos.BOTTOM_LEFT);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 1, 6);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
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

    public void clearFields() {
        selectedBookTextField.clear();
        quantitySpinner.getValueFactory().setValue(1);
        employeeComboBox.getSelectionModel().clearSelection();
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyBookButton.setOnAction(buyButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public Book getSelectedBook() {
        return bookTable.getSelectionModel().getSelectedItem();
    }

    public ComboBox<Long> getEmployeeComboBox() {
        return employeeComboBox;
    }

    public void setActionTargetText(String text) {
        this.actionTarget.setText(text);
    }

    public Stage getCustomerStage() {
        return customerStage;
    }
}
