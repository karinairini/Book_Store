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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;

import java.util.List;

public class AdministratorView {
    private Stage administratorStage;
    private ComponentFactory componentFactory;
    private GridPane gridPane;
    private Button manageUsersButton;
    private Button generatePDFButton;
    private Button logOutButton;
    private ComboBox<Long> employeeComboBox;

    public AdministratorView(Stage primaryStage, ComponentFactory componentFactory) {
        this.administratorStage = primaryStage;
        this.componentFactory = componentFactory;

        primaryStage.setTitle("Hello, Administrator!");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle();

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
        gridPane.add(sceneTitle, 1, 1, 3, 1);
        GridPane.setHalignment(sceneTitle, HPos.LEFT);
    }
    private void initializeFixedFields() {
        manageUsersButton = new Button("Manage users");
        manageUsersButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(manageUsersButton, 1, 5);
        GridPane.setHalignment(manageUsersButton, HPos.RIGHT);

        generatePDFButton = new Button("Generate PDF");
        generatePDFButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(generatePDFButton, 2, 7);
        GridPane.setHalignment(generatePDFButton, HPos.RIGHT);

        Label selectedEmployeeLabel = new Label("Selected employee:");
        selectedEmployeeLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(selectedEmployeeLabel, 0, 7);

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
        gridPane.add(employeeComboBox, 1, 7);

        logOutButton = new Button("LogOut");
        logOutButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(logOutButton, 1, 9);
            GridPane.setHalignment(logOutButton, HPos.CENTER);
    }
    public void addManageUsersButtonListener(EventHandler<ActionEvent> manageUsersButtonListener) {
        manageUsersButton.setOnAction(manageUsersButtonListener);
    }

    public void addGeneratePDFButtonListener(EventHandler<ActionEvent> generatePDFButtonListener) {
        generatePDFButton.setOnAction(generatePDFButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public ComboBox<Long> getEmployeeComboBox() {
        return employeeComboBox;
    }

    public Stage getAdministratorStage() {
        return administratorStage;
    }
 }
