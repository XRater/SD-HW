package spbau.xrater.interpretation.commands;

import spbau.xrater.interpretation.commands.commandUnits.CommandUnit;
import org.jetbrains.annotations.NotNull;

/**
 * Represents chain of simple commands (piped chain)
 */
public interface Command {

    /**
     * Adds unit command to the chain
     *
     * @param unitCommand unit command to add
     */
    void addCommandUnit(@NotNull CommandUnit unitCommand);

    /**
     * Consequently executes every unit command from the commands chain.
     *
     * @throws CommandExecutionException if exception happened during inner command execution
     */
    void run() throws CommandExecutionException;
}
