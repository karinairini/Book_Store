package repository.user;

import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean save(User user);

    boolean updatePassword(User user);

    boolean updateUsername(User user);

    void remove(Long id);

    void removeAll();

    String getSaltByUsername(String username);
}
