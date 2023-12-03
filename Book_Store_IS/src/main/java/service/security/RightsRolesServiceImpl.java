package service.security;

import model.Role;
import model.User;
import repository.security.RightsRolesRepository;

import java.util.List;

public class RightsRolesServiceImpl implements RightsRolesService {
    private final RightsRolesRepository rightsRolesRepository;
    public RightsRolesServiceImpl(RightsRolesRepository rightsRolesRepository) {
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        return rightsRolesRepository.findRolesForUser(userId);
    }

    @Override
    public Role findRoleByTitle(String role) {
        return rightsRolesRepository.findRoleByTitle(role);
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        rightsRolesRepository.addRolesToUser(user, roles);
    }
}
