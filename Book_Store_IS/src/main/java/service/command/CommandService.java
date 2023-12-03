package service.command;

import model.Command;

import java.util.List;
import java.util.Optional;

public interface CommandService {
    List<Command> findAll();

    List<Command> findByEmployeeId(Long employeeId);

    List<Command> findByCustomerId(Long customerId);

    Optional<Command> findById(Long id);

    boolean save(Command command);

    void removeAll();
}
