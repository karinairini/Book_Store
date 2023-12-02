package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import launcher.ComponentFactory;
import model.User;

import java.util.Arrays;
import java.util.List;

public class ManageUserView {
    private ComponentFactory componentFactory;
    private GridPane gridPane;
    private TableView<User> userTable;
    private TextField usernameTextField;
    private TextField passwordTextField;
    private TextField idTextField;
    private ComboBox<String> rolesComboBox;
    private ComboBox<String> additionalRolesComboBox;
    private Button createUserButton;
    private Button deleteUserButton;
    private Button updateUserButton;
    private Text actionTarget;

    public ManageUserView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;

        primaryStage.setTitle("User Management");

        gridPane = new GridPane();
        initializeGridPane();

        Scene manageEmployeesScene = new Scene(gridPane, 900, 500);
        primaryStage.setScene(manageEmployeesScene);

        initializeTable();

        //addRecordsToTable();

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

    private void initializeTable() {
        TableColumn<User, Long> idUser = new TableColumn<>("ID");
        idUser.setCellValueFactory(new PropertyValueFactory<>("id"));
        idUser.setMinWidth(45);

        TableColumn<User, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        username.setMinWidth(200);

        TableColumn<User, String> password = new TableColumn<>("Password");
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        password.setMinWidth(300);

        TableColumn<User, List<String>> roles = new TableColumn<>("Roles");
        roles.setCellValueFactory(new PropertyValueFactory<>("roles"));
        roles.setMinWidth(300);

        userTable = new TableView<>();
        userTable.getColumns().addAll(Arrays.asList(idUser, username, password, roles));
        addRecordsToTable();
        gridPane.add(userTable, 0, 1, 2, 1);

    }

    private void initializeVariableFields() {
        Label idLabel = new Label("ID:");
        idLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        idTextField = new TextField();
        idTextField.setEditable(false);
        idTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));

        HBox idBox = new HBox(10);
        idBox.getChildren().addAll(idLabel, idTextField);
        gridPane.add(idBox, 0, 4);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        usernameTextField = new TextField();
        usernameTextField.setEditable(true);
        usernameTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));

        HBox usernameBox = new HBox(10);
        usernameBox.getChildren().addAll(usernameLabel, usernameTextField);
        gridPane.add(usernameBox, 0, 5);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        passwordTextField = new TextField();
        passwordTextField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));

        HBox passwordBox = new HBox(10);
        passwordBox.getChildren().addAll(passwordLabel, passwordTextField);
        gridPane.add(passwordBox, 0, 6);

        Label rolesLabel = new Label("Roles:");
        rolesLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        rolesComboBox = new ComboBox<>(FXCollections.observableArrayList("employee", "customer"));
        additionalRolesComboBox = new ComboBox<>(FXCollections.observableArrayList("employee", "customer"));

        HBox rolesBox = new HBox(10);
        rolesBox.getChildren().addAll(rolesLabel, rolesComboBox, additionalRolesComboBox);
        gridPane.add(rolesBox, 0, 7);

        userTable.setOnMouseClicked((MouseEvent event) -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                idTextField.setText(String.valueOf(selectedUser.getId()));
                usernameTextField.setText(selectedUser.getUsername());
                rolesComboBox.setValue(componentFactory.getRightsRolesRepository().findRolesForUser(selectedUser.getId()).get(0).getRole());
                if (componentFactory.getRightsRolesRepository().findRolesForUser(selectedUser.getId()).size() == 2) {
                    additionalRolesComboBox.setValue(componentFactory.getRightsRolesRepository().findRolesForUser(selectedUser.getId()).get(1).getRole());
                }
            }
        });
    }

    private void initializeFixedFields() {
        createUserButton = new Button("Create User");
        createUserButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox createUserButtonHBox = new HBox(10);
        createUserButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        createUserButtonHBox.getChildren().add(createUserButton);
        gridPane.add(createUserButtonHBox, 1, 4);

        deleteUserButton = new Button("Delete User");
        deleteUserButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox deleteUserButtonHBox = new HBox(10);
        deleteUserButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        deleteUserButtonHBox.getChildren().add(deleteUserButton);
        gridPane.add(deleteUserButtonHBox, 1, 5);

        updateUserButton = new Button("Update User");
        updateUserButton.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox updateUserButtonHBox = new HBox(10);
        updateUserButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        updateUserButtonHBox.getChildren().add(updateUserButton);
        gridPane.add(updateUserButtonHBox, 1, 6);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        gridPane.add(actionTarget, 1, 7);
    }

    private ObservableList<User> getUser() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(componentFactory.getUserRepository().findAll());

        return users;
    }

    public void addRecordsToTable() {
        userTable.setItems(getUser());
    }

    public void addCreateUserButtonListener(EventHandler<ActionEvent> createUserButtonListener) {
        createUserButton.setOnAction(createUserButtonListener);
    }

    public void addDeleteUserButtonListener(EventHandler<ActionEvent> deleteUserButtonListener) {
        deleteUserButton.setOnAction(deleteUserButtonListener);
    }

    public void addUpdateUserButtonListener(EventHandler<ActionEvent> updateUserButtonListener) {
        updateUserButton.setOnAction(updateUserButtonListener);
    }

    public TextField getIdTextField() {
        return idTextField;
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }

    public ComboBox<String> getRolesComboBox() {
        return rolesComboBox;
    }

    public ComboBox<String> getAdditionalRolesComboBox() {
        return additionalRolesComboBox;
    }

    public void setActionTargetText(String text) {
        this.actionTarget.setText(text);
    }

}
