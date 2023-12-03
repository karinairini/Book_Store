package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AdministratorView {
    private final Stage administratorStage;
    private final GridPane gridPane;
    private Button manageUsersButton;
    private Button viewEmployeeActivityButton;
    private Button logOutButton;
    private Text actionTarget;

    public AdministratorView(Stage primaryStage) {
        this.administratorStage = primaryStage;

        primaryStage.setTitle("Hello, Administrator!");

        gridPane = new GridPane();
        initializeGridPane();

        Scene scene = new Scene(gridPane, 620, 380);
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
        Text sceneTitle = new Text("Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 1, 3, 1);
        GridPane.setHalignment(sceneTitle, HPos.CENTER);
    }

    private void initializeFields() {
        manageUsersButton = createButton("Manage users", 5);
        viewEmployeeActivityButton = createButton("View employee's activity", 7);

        logOutButton = createButton("LogOut", 9);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        gridPane.add(actionTarget, 0, 12, 3, 1);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
    }

    private Button createButton(String text, int row) {
        Button button = new Button(text);
        button.setFont(Font.font("Tahome", FontWeight.NORMAL, 14));
        HBox buttonHBox = new HBox(10);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.getChildren().add(button);
        gridPane.add(buttonHBox, 1, row);
        return button;
    }

    public void setActionTargetText(String text) {
        this.actionTarget.setText(text);
    }

    public void addManageUsersButtonListener(EventHandler<ActionEvent> manageUsersButtonListener) {
        manageUsersButton.setOnAction(manageUsersButtonListener);
    }

    public void addViewEmployeeActivityButtonListener(EventHandler<ActionEvent> viewEmployeeActivityButtonListener) {
        viewEmployeeActivityButton.setOnAction(viewEmployeeActivityButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public Stage getAdministratorStage() {
        return administratorStage;
    }
}
