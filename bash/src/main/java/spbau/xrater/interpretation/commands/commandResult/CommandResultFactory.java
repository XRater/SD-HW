package spbau.xrater.interpretation.commands.commandResult;

/**
 * Factory class to create command results (see {@link CommandResult})
 */
public class CommandResultFactory {

    /**
     * Creates new successful command result
     *
     * @param result result of command
     * @return new successful command result
     */
    public static CommandResult createSuccessfulCommandResult(final String result) {
        return new DummyCommandResult(result, null);
    }

    /**
     * Creates new unsuccessful command result
     *
     * @param exception exception happened during command execution
     * @return new unsuccessful command result
     */
    public static CommandResult createUnsuccessfulCommandResult(final Exception exception) {
        return new DummyCommandResult(null, exception);
    }

    private static class DummyCommandResult implements CommandResult {

        private final Exception exception;
        private final String result;

        DummyCommandResult(final String result, final Exception exception) {
            this.result = result;
            this.exception = exception;
        }

        @Override
        public boolean isValid() {
            return exception == null;
        }

        @Override
        public String getResult() {
            return result;
        }

        @Override
        public Exception getException() {
            return exception;
        }
    }

}
