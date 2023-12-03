package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import model.Role;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.security.PasswordGenerator;
import view.ManageUsersView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageUsersController {
    private final ManageUsersView manageUsersViewScene;
    private final ComponentFactory componentFactory;

    public ManageUsersController(ManageUsersView manageUserViewScene, ComponentFactory componentFactory) {
        this.manageUsersViewScene = manageUserViewScene;
        this.componentFactory = componentFactory;

        this.manageUsersViewScene.addCreateUserButtonListener(new CreateUserButtonListener());
        this.manageUsersViewScene.addDeleteUserButtonListener(new DeleteUserButtonListener());
        this.manageUsersViewScene.addUpdateUserPasswordButtonListener(new UpdateUserPasswordButtonListener());
        this.manageUsersViewScene.addUpdateUserUsernameButtonListener(new UpdateUserUsernameButtonListener());
        this.manageUsersViewScene.addUpdateUserRolesButtonListener(new UpdateUserRolesButtonListener());
    }

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();

        if (manageUsersViewScene.getRolesComboBox() != null && manageUsersViewScene.getRolesComboBox().getValue() != null && !manageUsersViewScene.getRolesComboBox().getValue().equals("")) {
            roles.add(componentFactory.getRightsRolesRepository().findRoleByTitle(manageUsersViewScene.getRolesComboBox().getValue()));
        }

        if (manageUsersViewScene.getAdditionalRolesComboBox() != null && manageUsersViewScene.getAdditionalRolesComboBox().getValue() != null && !manageUsersViewScene.getAdditionalRolesComboBox().getValue().equals("")) {
            roles.add(componentFactory.getRightsRolesRepository().findRoleByTitle(manageUsersViewScene.getAdditionalRolesComboBox().getValue()));
        }
        return roles;
    }

    private class CreateUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = manageUsersViewScene.getUsernameTextField().getText();
            String password = manageUsersViewScene.getPasswordTextField().getText();
            List<Role> roles = getRoles();
            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);

            if (registerNotification.hasErrors()) {
                manageUsersViewScene.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                manageUsersViewScene.setActionTargetText("Register successful!");
            }

            User user = componentFactory.getUserService().findByUsername(username).orElse(null);
            if (user != null) {
                componentFactory.getRightsRolesRepository().addRolesToUser(user, roles);
            }
            manageUsersViewScene.addRecordsToTable();

        }
    }

    private class DeleteUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            User user = manageUsersViewScene.getSelectedUser();

            componentFactory.getUserService().remove(user.getId());

            manageUsersViewScene.setActionTargetText("Deleted user successfully!");
            manageUsersViewScene.addRecordsToTable();
        }
    }

    private class UpdateUserPasswordButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String password = manageUsersViewScene.getPasswordTextField().getText();
            Long id = Long.parseLong(manageUsersViewScene.getIdTextField().getText());
            User user = manageUsersViewScene.getSelectedUser();

            user.setPassword(password);

            Notification<User> updateUserNotification = new Notification<>();

            UserValidator userValidator = new UserValidator(user);
            boolean validator = userValidator.validate();

            if (!validator) {
                userValidator.getErrors().forEach(updateUserNotification::addError);
                manageUsersViewScene.setActionTargetText(userValidator.getFormattedErrors());
            } else {
                user.setSalt(PasswordGenerator.generateSalt());
                user.setPassword(PasswordGenerator.generatePassword(user.getPassword(), user.getSalt()));
                if (componentFactory.getUserService().updatePassword(user)) {
                    manageUsersViewScene.setActionTargetText("Updated password successfully!");
                }
                manageUsersViewScene.addRecordsToTable();
            }
        }
    }

    private class UpdateUserUsernameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = manageUsersViewScene.getUsernameTextField().getText();
            User user = manageUsersViewScene.getSelectedUser();

            user.setUsername(username);

            Notification<User> updateUserNotification = new Notification<>();

            UserValidator userValidator = new UserValidator(user);
            boolean validator = userValidator.validate();

            if (!validator) {
                userValidator.getErrors().forEach(updateUserNotification::addError);
                manageUsersViewScene.setActionTargetText(userValidator.getFormattedErrors());
            } else if (componentFactory.getUserService().existsByUsername(username)) {
                updateUserNotification.addError("Email already registered!");
                manageUsersViewScene.setActionTargetText(updateUserNotification.getFormattedErrors());
            } else {
                if (componentFactory.getUserService().updateUsername(user)) {
                    manageUsersViewScene.setActionTargetText("Updated username successfully!");
                }
            }
            manageUsersViewScene.addRecordsToTable();
        }
    }

    private class UpdateUserRolesButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            List<Role> roles = getRoles();
            User user = manageUsersViewScene.getSelectedUser();

            componentFactory.getUserService().updateRoles(user, roles);
            manageUsersViewScene.addRecordsToTable();
        }
    }
}
