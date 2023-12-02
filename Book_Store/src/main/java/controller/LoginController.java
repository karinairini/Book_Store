package controller;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Role;
import model.User;
import model.validator.Notification;
import view.AdministratorView;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class LoginController {
    private final LoginView loginView;
    private final ComponentFactory componentFactory;

    public LoginController(LoginView loginView, ComponentFactory componentFactory) {
        this.loginView = loginView;
        this.componentFactory = componentFactory;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = componentFactory.getAuthenticationService().login(username, password);
            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText("Invalid Username or password!");
            } else {
                List<String> roles = loginNotification.getResult().getRoles().stream()
                        .map(Role::getRole)
                        .toList();

                if (roles.contains(Constants.Roles.ADMINISTRATOR)) {
                    loginView.getLoginStage().close();
                    AdministratorView administratorView = new AdministratorView(new Stage(), componentFactory);
                    AdministratorController administratorController = new AdministratorController(administratorView, componentFactory, loginNotification.getResult());
                } else if (roles.contains(Constants.Roles.EMPLOYEE) && roles.contains(Constants.Roles.CUSTOMER)) {

                    List<String> choices = Arrays.asList("Employee", "Customer");

                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Role Selection");
                    dialog.setHeaderText("Choose a role to proceed:");

                    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableList(choices));
                    dialog.getDialogPane().setContent(comboBox);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okButtonType) {
                            return comboBox.getValue();
                        }
                        return null;
                    });

                    Optional<String> result = dialog.showAndWait();

                    result.ifPresent(selectedRole -> {
                        if ("Employee".equals(selectedRole)) {
                            EmployeeView employeeView = new EmployeeView(new Stage(), componentFactory);
                            EmployeeController employeeController = new EmployeeController(employeeView, componentFactory, loginNotification.getResult());
                        } else if ("Customer".equals(selectedRole)) {
                            CustomerView customerView = new CustomerView(new Stage(), componentFactory);
                            CustomerController customerController = new CustomerController(customerView, componentFactory, loginNotification.getResult());
                        }
                    });
                } else if (roles.contains(Constants.Roles.EMPLOYEE)) {
                    loginView.getLoginStage().close();
                    EmployeeView employeeView = new EmployeeView(new Stage(), componentFactory);
                    EmployeeController employeeController = new EmployeeController(employeeView, componentFactory, loginNotification.getResult());
                } else {
                    loginView.getLoginStage().close();
                    CustomerView customerView = new CustomerView(new Stage(), componentFactory);
                    CustomerController customerController = new CustomerController(customerView, componentFactory, loginNotification.getResult());
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);
            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
