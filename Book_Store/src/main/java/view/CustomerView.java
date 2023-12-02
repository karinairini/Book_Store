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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;

import javafx.scene.input.MouseEvent;
import model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CustomerView {
    private Stage customerStage;
    private ComponentFactory componentFactory;
    private GridPane gridPane;
    private TableView<Book> bookTable;
    private TextField idBookTextField;
    private TextField selectedBookTextField;
    private Spinner<Integer> quantitySpinner;
    private ComboBox<Long> employeeComboBox;
    private Button addToCartButton;
    private Button viewCartButton;
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
        gridPane.add(bookTable, 0, 2, 2, 1);
    }

    private void initializeVariableFields() {
        Label idBookLabel = new Label("ID Book:");
        idBookLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        idBookTextField = new TextField();
        idBookTextField.setEditable(false);
        idBookTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        idBookTextField.setPrefWidth(50);

        HBox idBookBox = new HBox(10);
        idBookBox.getChildren().addAll(idBookLabel, idBookTextField);
        gridPane.add(idBookBox, 0, 3);

        Label selectedBookLabel = new Label("Selected book:");
        selectedBookLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        selectedBookTextField = new TextField();
        selectedBookTextField.setEditable(false);
        selectedBookTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        selectedBookTextField.setPrefWidth(200);

        HBox selectedBookBox = new HBox(10);
        selectedBookBox.getChildren().addAll(selectedBookLabel, selectedBookTextField);

        bookTable.setOnMouseClicked((MouseEvent event) -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                selectedBookTextField.setText(selectedBook.getTitle());
                idBookTextField.setText(selectedBook.getId().toString());
            }
        });

        gridPane.add(selectedBookBox, 0, 4);

        Label selectedQuantityLabel = new Label("Selected quantity:");
        selectedQuantityLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        quantitySpinner = new Spinner<>(1, 100, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(120);

        HBox selectedQuantityBox = new HBox(10);
        selectedQuantityBox.getChildren().addAll(selectedQuantityLabel, quantitySpinner);

        gridPane.add(selectedQuantityBox, 0, 5);

        Label selectedEmployeeLabel = new Label("Selected employee:");
        selectedEmployeeLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        employeeComboBox = new ComboBox<>();
        List<Long> employeeIDList = componentFactory.getUserRepository()
                .findAll()
                .stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                .map(User::getId)
                .toList();

        ObservableList<Long> observableEmployeeIDList = FXCollections.observableArrayList(employeeIDList);
        employeeComboBox.setItems(observableEmployeeIDList);
        employeeComboBox.setPrefWidth(100);

        HBox employeeIDBox = new HBox(10);
        employeeIDBox.getChildren().addAll(selectedEmployeeLabel, employeeComboBox);

        gridPane.add(employeeIDBox, 0, 6);
    }

    private void initializeFixedFields() {
        addToCartButton = new Button("Add to cart");
        addToCartButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(addToCartButton, 1, 5);
        GridPane.setHalignment(addToCartButton, HPos.RIGHT);

        logOutButton = new Button("LogOut");
        logOutButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(logOutButton, 0, 0);
        GridPane.setHalignment(logOutButton, HPos.LEFT);

        viewCartButton = new Button("View cart");
        viewCartButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(viewCartButton, 1, 0);
        GridPane.setHalignment(viewCartButton, HPos.RIGHT);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 1, 6);
    }

    private ObservableList<Book> getBook() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());
        books.removeIf(book -> book.getStock() == 0);

        books.forEach(book -> {
            book.setAge(componentFactory.getBookService().getAgeOfBook(book.getId()));
        });
        return books;
    }

    public void addRecordsToTable() {
        bookTable.setItems(getBook());
    }

    public Spinner<Integer> getQuantitySpinner() {
        return quantitySpinner;
    }

    public void addAddToCartButtonListener(EventHandler<ActionEvent> addToCartButtonListener) {
        addToCartButton.setOnAction(addToCartButtonListener);
    }

    public void addViewCartButtonListener(EventHandler<ActionEvent> viewCartButtonListener) {
        viewCartButton.setOnAction(viewCartButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public Book getSelectedBook() {
        return bookTable.getSelectionModel().getSelectedItem();
    }
    public TextField getIdBookTextField() {
        return idBookTextField;
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
