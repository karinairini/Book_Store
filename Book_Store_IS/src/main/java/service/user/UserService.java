package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    boolean save(User user);

    boolean updatePassword(User user);

    boolean updateUsername(User user);

    void updateRoles(User user, List<Role> roles);

    void remove(Long id);

    void removeAll();
}
