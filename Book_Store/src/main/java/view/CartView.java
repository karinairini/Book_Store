package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Cart;
import model.User;

import java.util.Arrays;


public class CartView {
    private Stage cartStage;
    private ComponentFactory componentFactory;
    private User user;
    private GridPane gridPane;
    private TableView<Cart> cartTable;
    private TableColumn<Cart, Integer> quantityCart;
    private Button returnBookToLibraryButton;
    private Button continueShoppingButton;
    private Button buyButton;
    private Label totalPriceLabel;
    private int decision;


    public CartView(Stage primaryStage, ComponentFactory componentFactory, int decision, User user) {
        this.cartStage = primaryStage;
        this.componentFactory = componentFactory;
        this.decision = decision;
        this.user = user;

        primaryStage.setTitle("Cart");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 600, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle();

        initializeTable();

        addRecordsToTable();

        initializeFields();

        primaryStage.show();
    }

    private void initializeGridPane() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle() {
        Text sceneTitle = new Text("Your Cart");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
    }

    private void initializeTable() {
        TableColumn<Cart, String> bookTitleCart = new TableColumn<>("Book Title");
        bookTitleCart.setMinWidth(200);
        bookTitleCart.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        quantityCart = new TableColumn<>("Quantity");
        quantityCart.setMinWidth(100);
        quantityCart.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        cartTable = new TableView<>();
        addRecordsToTable();
        cartTable.getColumns().addAll(Arrays.asList(bookTitleCart, quantityCart));
        gridPane.add(cartTable, 0, 2, 2, 1);
    }

    public void initializeFields() {
        returnBookToLibraryButton = new Button("Return book to library");
        returnBookToLibraryButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(returnBookToLibraryButton, 1, 5);
        GridPane.setHalignment(returnBookToLibraryButton, HPos.RIGHT);

        continueShoppingButton = new Button("Continue shopping");
        continueShoppingButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(continueShoppingButton, 0, 5);
        GridPane.setHalignment(continueShoppingButton, HPos.LEFT);

        buyButton = new Button("Buy");
        buyButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(buyButton, 1, 0);
        GridPane.setHalignment(buyButton, HPos.RIGHT);

        totalPriceLabel = new Label();
        totalPriceLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(totalPriceLabel, 0, 6, 2, 1);
        GridPane.setHalignment(totalPriceLabel, HPos.CENTER);
    }

    public ObservableList<Cart> getCartItems() {
        ObservableList<Cart> cartItems = FXCollections.observableArrayList();
        cartItems.addAll(componentFactory.getCartService().findAll(user));
        return cartItems;
    }

    public void addRecordsToTable() {
        cartTable.setItems(getCartItems());
    }
    public void emptyTable() {
        cartTable.setItems(FXCollections.observableArrayList());
    }

    public void addReturnBookToLibraryButtonListener(EventHandler<ActionEvent> returnBookToLibraryButtonListener) {
        returnBookToLibraryButton.setOnAction(returnBookToLibraryButtonListener);
    }

    public void addContinueShoppingButtonListener(EventHandler<ActionEvent> continueShoppingButtonListener) {
        continueShoppingButton.setOnAction(continueShoppingButtonListener);
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }

    public void setTotalPriceLabel(String totalPriceLabel) {
        this.totalPriceLabel.setText("Total price: " + totalPriceLabel);
    }

    public Cart getSelectedCartItem() {
        return cartTable.getSelectionModel().getSelectedItem();
    }

    public Stage getCartStage() {
        return cartStage;
    }

    public int getDecision() {
        return decision;
    }
}
