package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;

/**
 * Interface for one command unit. May be executed on string input
 */
public interface CommandUnit {

    /**
     * Executes command on target input
     *
     * @param input input to execute on
     * @return result of command execution
     */
    CommandResult execute(String input);

}
