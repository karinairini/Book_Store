package view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView {
    private final Stage loginStage;
    private final GridPane gridPane;
    private TextField userTextField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button logInButton;
    private Text actionTarget;

    public LoginView(Stage primaryStage) {
        loginStage = primaryStage;
        primaryStage.setTitle("Book Store");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle();

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
        Text sceneTitle = new Text("Welcome to our Book Store!");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields() {
        Label userName = new Label("User Name:");
        gridPane.add(userName, 0, 1);

        userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label password = new Label("Password");
        gridPane.add(password, 0, 2);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        signInButton = new Button("Sign In");
        HBox signInButtonHBox = new HBox(10);
        signInButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        signInButtonHBox.getChildren().add(signInButton);
        gridPane.add(signInButtonHBox, 1, 4);

        logInButton = new Button("Log In");
        HBox logInButtonHBox = new HBox(10);
        logInButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logInButtonHBox.getChildren().add(logInButton);
        gridPane.add(logInButtonHBox, 0, 4);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        gridPane.add(actionTarget, 1, 6);

        userTextField.setText("i.k@gmail.com");
        passwordField.setText("parola1234!");
    }

    public String getUsername() {
        return userTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setActionTargetText(String text) {
        this.actionTarget.setText(text);
    }

    public void addLoginButtonListener(EventHandler<ActionEvent> loginButtonListener) {
        logInButton.setOnAction(loginButtonListener);
    }

    public void addRegisterButtonListener(EventHandler<ActionEvent> signInButtonListener) {
        signInButton.setOnAction(signInButtonListener);
    }

    public Stage getLoginStage() {
        return this.loginStage;
    }
}
