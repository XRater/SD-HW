package spbau.xrater.interpretation.instruction;

import spbau.xrater.interpretation.Session;
import spbau.xrater.interpretation.commands.Command;
import spbau.xrater.interpretation.commands.CommandExecutionException;
import spbau.xrater.interpretation.commands.CommandFactory;
import spbau.xrater.interpretation.commands.commandUnits.CommandUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents list of commands to execute consequently
 */
public class CommandInstruction implements Instruction {

    private final Command command;

    CommandInstruction() {
        this.command = CommandFactory.createNewCommand();
    }

    public void addUnitCommand(final CommandUnit commandUnit) {
        this.command.addCommandUnit(commandUnit);
    }

    /**
     * @param session session to execute instruction in
     * @throws CommandExecutionException if exception happened during execution
     */
    @Override
    public void execute(@NotNull final Session session) throws CommandExecutionException {
        command.run();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CommandInstruction) {
            return command.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(command);
    }
}
