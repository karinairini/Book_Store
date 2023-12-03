package service.command;

import model.Command;
import repository.command.CommandRepository;

import java.util.List;
import java.util.Optional;

public class CommandServiceImpl implements CommandService {
    private final CommandRepository commandRepository;

    public CommandServiceImpl(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    @Override
    public List<Command> findAll() {
        return commandRepository.findAll();
    }

    @Override
    public List<Command> findByEmployeeId(Long employeeId) {
        return commandRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Command> findByCustomerId(Long customerId) {
        return commandRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Command> findById(Long id) {
        return commandRepository.findById(id);
    }

    @Override
    public boolean save(Command command) {
        return commandRepository.save(command);
    }

    @Override
    public void removeAll() {
        commandRepository.removeAll();
    }
}
