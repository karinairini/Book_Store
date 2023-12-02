package repository.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {
    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean save(User user);

    void removeAll();

    Optional<User> existsByUsername(String username);

    void remove(Long id);

    String getSaltByUsername(String username);

    boolean updateUser(User user);

    Optional<User> findById(Long id);
}
