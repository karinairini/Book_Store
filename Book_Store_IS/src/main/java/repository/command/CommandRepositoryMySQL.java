package repository.command;

import model.Command;
import model.builder.CommandBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandRepositoryMySQL implements CommandRepository {
    private final Connection connection;

    public CommandRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Command> findAll() {
        String sql = "SELECT * FROM command;";
        List<Command> commands = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                commands.add(getCommandFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    @Override
    public List<Command> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM command WHERE employee_id = ?;";
        List<Command> commands = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                commands.add(getCommandFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    @Override
    public List<Command> findByCustomerId(Long customerId) {
        String sql = "SELECT * FROM command WHERE customer_id = ?;";
        List<Command> commands = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                commands.add(getCommandFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    @Override
    public Optional<Command> findById(Long id) {
        String sql = "SELECT * FROM command WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Command command = getCommandFromResultSet(resultSet);
                return Optional.of(command);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Command command) {
        String sql = "INSERT IGNORE INTO command VALUES(null, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, command.getEmployeeId());
            preparedStatement.setLong(2, command.getCustomerId());
            preparedStatement.setLong(3, command.getBookId());
            preparedStatement.setInt(4, command.getQuantity());
            preparedStatement.setDouble(5, command.getPrice());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM command WHERE id >= 0;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Command getCommandFromResultSet(ResultSet resultSet) throws SQLException {
        return new CommandBuilder()
                .setId(resultSet.getLong("id"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .setCustomerId(resultSet.getLong("customer_id"))
                .setBookId(resultSet.getLong("book_id"))
                .setQuantity(resultSet.getInt("quantity"))
                .setPrice(resultSet.getDouble("price"))
                .build();
    }
}
