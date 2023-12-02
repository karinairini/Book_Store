package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Notification<User> findByUsernameAndPassword(User user);

    boolean save(User user);

    void removeAll();

    Optional<User> existsByUsername(User user);

    void remove(Long id);

    String getSaltByUsername(User user);

    Notification<User> updateUser(User user, List<Role> roles);

    Optional<User> findById(Long id);
}
