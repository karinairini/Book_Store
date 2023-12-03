package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.RIGHT;
import static database.Constants.Tables.ROLE;
import static database.Constants.Tables.ROLE_RIGHT;
import static database.Constants.Tables.USER_ROLE;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {
    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(String role) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT IGNORE INTO " + ROLE + " VALUES (null, ?)");
            insertStatement.setString(1, role);

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addRight(String right) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT IGNORE INTO `" + RIGHT + "` VALUES (null, ?)");
            insertStatement.setString(1, right);

            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Role findRoleByTitle(String role) {
        try {
            String sql = "SELECT * FROM " + ROLE + " WHERE role = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, role);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            Long roleId = resultSet.getLong("id");
            String roleTitle = resultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Role findRoleById(Long roleId) {
        try {
            String sql = "SELECT * FROM " + ROLE + " WHERE id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, roleId);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            String roleTitle = resultSet.getString("role");
            return new Role(roleId, roleTitle, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Right findRightByTitle(String right) {
        try {
            String sql = "SELECT * FROM `" + RIGHT + "` WHERE `right` = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, right);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            Long rightId = resultSet.getLong("id");
            String rightTitle = resultSet.getString("right");
            return new Right(rightId, rightTitle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        try {
            user.setRoles(new ArrayList<>());
            removeRolesFromUser(user);
            for (Role role : roles) {
                PreparedStatement insertUserRoleStatement = connection
                        .prepareStatement("INSERT INTO `user_role` VALUES (null, ?, ?)");
                insertUserRoleStatement.setLong(1, user.getId());
                insertUserRoleStatement.setLong(2, role.getId());
                insertUserRoleStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeRolesFromUser(User user) {
        try {
            String sql = "DELETE FROM `user_role` WHERE user_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        List<Role> roles = new ArrayList<>();

        try {
            String sql = "SELECT * FROM " + USER_ROLE + " WHERE user_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long roleId = resultSet.getLong("role_id");
                roles.add(findRoleById(roleId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ROLE_RIGHT + " VALUE (null, ?, ?)");
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
