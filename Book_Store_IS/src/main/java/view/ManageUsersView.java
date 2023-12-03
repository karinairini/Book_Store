package view;

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
import model.User;

import java.util.Arrays;
import java.util.List;

public class ManageUsersView {
    private final ComponentFactory componentFactory;
    private final GridPane gridPane;
    private TableView<User> userTable;
    private TextField usernameTextField;
    private TextField passwordTextField;
    private ComboBox<String> rolesComboBox;
    private ComboBox<String> additionalRolesComboBox;
    private Button createUserButton;
    private Button deleteUserButton;
    private Button updateUserPasswordButton;
    private Button updateUserUsernameButton;
    private Button updateUserRolesButton;
    private Text actionTarget;

    public ManageUsersView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;

        primaryStage.setTitle("User Management");

        gridPane = new GridPane();
        initializeGridPane();

        Scene manageEmployeesScene = new Scene(gridPane, 900, 500);
        primaryStage.setScene(manageEmployeesScene);

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

    private void initializeTable() {
        TableColumn<User, Long> idUser = createTableColumn("ID", 45, "id");
        TableColumn<User, String> username = createTableColumn("Username", 200, "username");
        TableColumn<User, String> password = createTableColumn("Password", 300, "password");
        TableColumn<User, List<String>> roles = createTableColumn("Roles", 300, "roles");

        userTable = new TableView<>();
        addRecordsToTable();
        userTable.getColumns().addAll(Arrays.asList(idUser, username, password, roles));
        gridPane.add(userTable, 0, 1, 2, 1);

        userTable.setOnMouseClicked((MouseEvent event) -> {
            User selectedUser = getSelectedUser();
            if (selectedUser != null) {
                setTextFieldValues(selectedUser);
            }
        });
    }

    private <S, T> TableColumn<S, T> createTableColumn(String text, double width, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(text);
        column.setMinWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private void setTextFieldValues(User selectedUser) {
        usernameTextField.setText(selectedUser.getUsername());
        rolesComboBox.setValue(componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).get(0).getRole());
        if (componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).size() == 2) {
            additionalRolesComboBox.setValue(componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).get(1).getRole());
        }
    }

    private void initializeVariableFields() {
        usernameTextField = createTextField("Username:", 5);
        passwordTextField = createTextField("Password:", 6);

        Label selectRolesLabel = new Label("Select roles:");
        selectRolesLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        rolesComboBox = new ComboBox<>(FXCollections.observableArrayList("", "employee", "customer"));
        additionalRolesComboBox = new ComboBox<>(FXCollections.observableArrayList(""));
        rolesComboBox.setOnAction(event -> {
            String selectedRole = rolesComboBox.getValue();

            additionalRolesComboBox.getItems().clear();
            if ("employee".equals(selectedRole)) {
                additionalRolesComboBox.getItems().addAll("", "customer");
            } else if ("customer".equals(selectedRole)) {
                additionalRolesComboBox.getItems().addAll("", "employee");
            }
        });

        HBox rolesBox = new HBox(10);
        rolesBox.getChildren().addAll(selectRolesLabel, rolesComboBox, additionalRolesComboBox);

        gridPane.add(rolesBox, 0, 7);

        userTable.setOnMouseClicked((MouseEvent event) -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                usernameTextField.setText(selectedUser.getUsername());
                rolesComboBox.setValue(componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).get(0).getRole());
                if (componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).size() == 2) {
                    additionalRolesComboBox.setValue(componentFactory.getRightsRolesService().findRolesForUser(selectedUser.getId()).get(1).getRole());
                }
            }
        });
    }

    private TextField createTextField(String label, int row) {
        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));

        TextField textField = new TextField();
        textField.setFont(Font.font("Tahome", FontWeight.NORMAL, 12));
        textField.setPrefWidth(200);

        HBox box = new HBox(10);
        box.getChildren().addAll(titleLabel, textField);

        gridPane.add(box, 0, row);

        return textField;
    }

    private void initializeFixedFields() {
        createUserButton = createButton("Create User", 4);
        deleteUserButton = createButton("Delete User", 5);
        updateUserPasswordButton = createButton("Update User's password", 8);
        updateUserUsernameButton = createButton("Update User's username", 7);
        updateUserRolesButton = createButton("Update User's roles", 6);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 1, 9, 1, 3);
        GridPane.setHalignment(actionTarget, HPos.LEFT);
    }

    private Button createButton(String text, int row) {
        Button button = new Button(text);
        button.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox buttonHBox = new HBox(10);
        buttonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonHBox.getChildren().add(button);
        gridPane.add(buttonHBox, 1, row);
        return button;
    }

    private ObservableList<User> getUser() {
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(componentFactory.getUserService().findAll());

        return users;
    }

    public void addRecordsToTable() {
        userTable.setItems(getUser());
    }

    public void clearFields() {
        usernameTextField.clear();
        passwordTextField.clear();
        rolesComboBox.getSelectionModel().clearSelection();
        additionalRolesComboBox.getSelectionModel().clearSelection();
    }

    public void addCreateUserButtonListener(EventHandler<ActionEvent> createUserButtonListener) {
        createUserButton.setOnAction(createUserButtonListener);
    }

    public void addDeleteUserButtonListener(EventHandler<ActionEvent> deleteUserButtonListener) {
        deleteUserButton.setOnAction(deleteUserButtonListener);
    }

    public void addUpdateUserPasswordButtonListener(EventHandler<ActionEvent> updateUserPasswordButtonListener) {
        updateUserPasswordButton.setOnAction(updateUserPasswordButtonListener);
    }

    public void addUpdateUserUsernameButtonListener(EventHandler<ActionEvent> updateUserUsernameButtonListener) {
        updateUserUsernameButton.setOnAction(updateUserUsernameButtonListener);
    }

    public void addUpdateUserRolesButtonListener(EventHandler<ActionEvent> updateUserRolesButtonListener) {
        updateUserRolesButton.setOnAction(updateUserRolesButtonListener);
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

    public User getSelectedUser() {
        return userTable.getSelectionModel().getSelectedItem();
    }
}
