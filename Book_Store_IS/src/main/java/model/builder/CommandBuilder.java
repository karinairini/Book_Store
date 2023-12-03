package model.builder;

import model.Command;

public class CommandBuilder {
    private Command command;

    public CommandBuilder() {
        command = new Command();
    }

    public CommandBuilder setId(Long id) {
        command.setId(id);
        return this;
    }

    public CommandBuilder setEmployeeId(Long employeeId) {
        command.setEmployeeId(employeeId);
        return this;
    }

    public CommandBuilder setCustomerId(Long customerId) {
        command.setCustomerId(customerId);
        return this;
    }

    public CommandBuilder setBookId(Long bookId) {
        command.setBookId(bookId);
        return this;
    }

    public CommandBuilder setQuantity(Integer quantity) {
        command.setQuantity(quantity);
        return this;
    }

    public CommandBuilder setPrice(Double price) {
        command.setPrice(price);
        return this;
    }

    public Command build() {
        return command;
    }
}
