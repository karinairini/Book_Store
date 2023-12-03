package repository.user;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryMySQL implements UserRepository {
    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user;";
        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?;";
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;
    }

    //Metoda reimplementata fara SQL injection
    @Override
    public boolean existsByUsername(String email) {
        String sql = "SELECT * FROM user WHERE username = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection.prepareStatement("INSERT IGNORE INTO user VALUES (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setString(3, user.getSalt());
            insertUserStatement.executeUpdate();

            ResultSet resultSet = insertUserStatement.getGeneratedKeys();
            resultSet.next();

            long userId = resultSet.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(User user) {
        String sql = "UPDATE user SET password = ?, salt = ? WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getSalt());
            preparedStatement.setLong(3, user.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUsername(User user) {
        String sql = "UPDATE user SET username = ? WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setLong(2, user.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM user WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM user WHERE id >= 0";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSaltByUsername(String username) {
        String sql = "SELECT salt FROM user WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("salt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .setSalt(resultSet.getString("salt"))
                .build();
    }
}
