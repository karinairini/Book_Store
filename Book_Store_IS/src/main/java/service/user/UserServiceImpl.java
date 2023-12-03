package service.user;

import model.Role;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public UserServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean updatePassword(User user) {
        return userRepository.updatePassword(user);
    }

    @Override
    public boolean updateUsername(User user) {
        return userRepository.updateUsername(user);
    }

    @Override
    public void updateRoles(User user, List<Role> roles) {
        rightsRolesRepository.addRolesToUser(user, roles);
    }

    @Override
    public void remove(Long id) {
        userRepository.remove(id);
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();
    }
}
