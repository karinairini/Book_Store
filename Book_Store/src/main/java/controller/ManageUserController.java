package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.ComponentFactory;
import model.Role;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.security.PasswordGenerator;
import view.ManageUserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageUserController {
    private final ManageUserView manageUserViewScene;
    private final ComponentFactory componentFactory;

    public ManageUserController(ManageUserView manageUserViewScene, ComponentFactory componentFactory) {
        this.manageUserViewScene = manageUserViewScene;
        this.componentFactory = componentFactory;

        this.manageUserViewScene.addCreateUserButtonListener(new CreateUserButtonListener());
        this.manageUserViewScene.addDeleteUserButtonListener(new DeleteUserButtonListener());
        this.manageUserViewScene.addUpdateUserButtonListener(new UpdateUserButtonListener());
    }

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();

        if (manageUserViewScene.getRolesComboBox().getValue() != null) {
            roles.add(componentFactory.getRightsRolesRepository().findRoleByTitle(manageUserViewScene.getRolesComboBox().getValue()));
        }
        if (manageUserViewScene.getAdditionalRolesComboBox().getValue() != null) {
            roles.add(componentFactory.getRightsRolesRepository().findRoleByTitle(manageUserViewScene.getAdditionalRolesComboBox().getValue()));
        }
        return roles;
    }

    private class CreateUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = manageUserViewScene.getUsernameTextField().getText();
            String password = manageUserViewScene.getPasswordTextField().getText();
            List<Role> roles = getRoles();
            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);

            if (registerNotification.hasErrors()) {
                manageUserViewScene.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                manageUserViewScene.setActionTargetText("Register successful!");
            }

            Optional<User> user = componentFactory.getUserRepository().existsByUsername(username);
            componentFactory.getRightsRolesRepository().addRolesToUser(user.get(), roles);
            manageUserViewScene.addRecordsToTable();

        }
    }

    private class DeleteUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = manageUserViewScene.getUsernameTextField().getText();

            Optional<User> user = componentFactory.getUserRepository().existsByUsername(username);

            componentFactory.getUserRepository().remove(user.get().getId());

            manageUserViewScene.setActionTargetText("Delete successful!");
            manageUserViewScene.addRecordsToTable();
        }
    }

    private class UpdateUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String password = manageUserViewScene.getPasswordTextField().getText();
            Long id = Long.parseLong(manageUserViewScene.getIdTextField().getText());
            User user = componentFactory.getUserRepository().findById(id).orElse(null);

            user.setPassword(password);

            Notification<User> updateUserNotification = new Notification<>();

            UserValidator userValidator = new UserValidator(user);
            boolean validator = userValidator.validate();

            if (!validator) {
                userValidator.getErrors().forEach(updateUserNotification::addError);
            } else {
                user.setSalt(PasswordGenerator.generateSalt());
                user.setPassword(PasswordGenerator.generatePassword(user.getPassword(), user.getSalt()));
                componentFactory.getUserRepository().updateUser(user);
                manageUserViewScene.addRecordsToTable();
            }
            /*String username = manageUserViewScene.getUsernameTextField().getText();
            String password = manageUserViewScene.getPasswordTextField().getText();
            Long id = Long.parseLong(manageUserViewScene.getIdTextField().getText());

            List<Role> roles = getRoles();

            User user = componentFactory.getUserRepository().findById(id).orElse(null);
            if(user.getUsername().equals(username)) {
                user.setPassword(password);

                Notification<User> updateUserNotification = new Notification<>();

                UserValidator userValidator = new UserValidator(user);
                boolean validator = userValidator.validate();

                if (!validator) {
                    userValidator.getErrors().forEach(updateUserNotification::addError);
                } else {
                    user.setSalt(PasswordGenerator.generateSalt());
                    user.setPassword(PasswordGenerator.generatePassword(user.getPassword(), user.getSalt()));
                    componentFactory.getUserRepository().updateUser(user, roles);
                }

                manageUserViewScene.addRecordsToTable();
                if (updateUserNotification.hasErrors()) {
                    manageUserViewScene.setActionTargetText(updateUserNotification.getFormattedErrors());
                } else {
                    manageUserViewScene.setActionTargetText("Update successful!");
                }
            } else if(!user.getUsername().equals(username)) {

            }*/
        }
    }
}
