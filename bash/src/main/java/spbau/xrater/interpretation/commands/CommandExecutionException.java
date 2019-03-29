package spbau.xrater.interpretation.commands;

/**
 * Exception happened during command execution
 */
public class CommandExecutionException extends Exception {

    CommandExecutionException(final Throwable cause) {
        super(cause);
    }
}
