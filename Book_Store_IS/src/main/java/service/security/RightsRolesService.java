package service.security;

import model.Role;
import model.User;

import java.util.List;

public interface RightsRolesService {
    List<Role> findRolesForUser(Long userId);

    Role findRoleByTitle(String role);

    void addRolesToUser(User user, List<Role> roles);
}
