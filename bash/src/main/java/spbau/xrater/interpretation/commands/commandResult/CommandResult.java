package spbau.xrater.interpretation.commands.commandResult;

/**
 * Represents result of command execution
 */
public interface CommandResult {

    /**
     * Checks if command ended successfully
     *
     * @return true if command ended successfully and false otherwise
     */
    boolean isValid();

    /**
     * Get execution result if successful. May throw exception if
     * execution was not successful.
     *
     * @return result of execution
     */
    String getResult();

    /**
     * Get exception happened during execution if present.
     *
     * @return returns the reason of unsuccessful command call. Returns null
     * if command call was successful.
     */
    Exception getException();
}
