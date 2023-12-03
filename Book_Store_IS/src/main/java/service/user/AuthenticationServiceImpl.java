package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.security.PasswordGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;

import static database.Constants.Roles.*;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {
        Notification<Boolean> userRegisterNotification = new Notification<>();
        Role employeeRole = rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();

        String salt = PasswordGenerator.generateSalt();
        String hashedPassword = PasswordGenerator.generatePassword(password, salt);

        user = new UserBuilder()
                .setUsername(username)
                .setPassword(hashedPassword)
                .setRoles(Collections.singletonList(employeeRole))
                .setSalt(salt)
                .build();

        if (userRepository.existsByUsername(username)) {
            userRegisterNotification.addError("Email already registered!");
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else if (!userValid) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            if (userRepository.save(user)) {
                userRegisterNotification.setResult(Boolean.TRUE);
            } else {
                userRegisterNotification.addError("Failed to register the user.");
            }
        }
        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        Notification<User> loginNotification = new Notification<>();
        String salt = userRepository.getSaltByUsername(username);

        if (salt == null) {
            loginNotification.addError("Invalid username or password!");
            return loginNotification;
        }

        String hashedPassword = PasswordGenerator.generatePassword(password, salt);
        return userRepository.findByUsernameAndPassword(username, hashedPassword);
    }
}
