package service.user;

import model.Role;
import model.User;
import model.validator.Notification;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<User> findByUsernameAndPassword(User user) {
        return null;
    }

    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public void removeAll() {

    }

    @Override
    public Optional<User> existsByUsername(User user) {
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public String getSaltByUsername(User user) {
        return null;
    }

    @Override
    public Notification<User> updateUser(User user, List<Role> roles) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }
}
