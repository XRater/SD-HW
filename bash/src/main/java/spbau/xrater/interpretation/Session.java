package spbau.xrater.interpretation;

import spbau.xrater.interpretation.commands.CommandExecutionException;
import org.jetbrains.annotations.NotNull;
import spbau.xrater.parsing.BashParseException;

/**
 * Interface for basic variant of session. May have state.
 */
public interface Session {

    /**
     * Executes given string as an instruction
     *
     * @param input string representation of instruction to execute
     * @throws BashParseException if string could not be parsed
     * @throws CommandExecutionException if exception happened during execution
     */
    void processInput(@NotNull String input) throws BashParseException, CommandExecutionException;

    /**
     * Sets scope variable
     *
     * @param name name target of variable
     * @param value value to set
     */
    void setVariable(@NotNull String name, @NotNull String value);
}
