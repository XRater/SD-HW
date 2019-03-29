package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

/**
 * This command calls for outer system command
 */
class SystemCommandUnit implements CommandUnit {

    private static final int BUF_SIZE = 1024;
    private final List<String> args;

    SystemCommandUnit(final List<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.args = args;
    }

    @Override
    public CommandResult execute(final String input) {
        final Process process;
        String result = "";
        String errors = "";
        try {
            process = new ProcessBuilder(args).start();
            process.waitFor();
            try (final Reader reader = new InputStreamReader(process.getInputStream())) {
                final char[] buffer = new char[BUF_SIZE];
                while (reader.read(buffer, 0, BUF_SIZE) > 0) {
                    result = result.concat(new String(buffer));
                }
            }
            try (final Reader reader = new InputStreamReader(process.getErrorStream())) {
                final char[] buffer = new char[BUF_SIZE];
                while (reader.read(buffer, 0, BUF_SIZE) > 0) {
                    errors = errors.concat(new String(buffer));
                }
            }
        } catch (final IOException | InterruptedException e) {
            return CommandResultFactory.createUnsuccessfulCommandResult(e);
        }
        final int exitValue = process.exitValue();
        if (exitValue == 0) {
            return CommandResultFactory.createSuccessfulCommandResult(result + '\n');
        } else {
            return CommandResultFactory.createUnsuccessfulCommandResult(new ProcessExecutionException(exitValue, errors));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SystemCommandUnit) {
            return Objects.equals(((SystemCommandUnit) obj).args, args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(args);
    }

    public static class ProcessExecutionException extends Exception {

        ProcessExecutionException(final int code, final String errors) {
            super("Process ended with non zero code: " + code
                    + System.lineSeparator() + errors);
        }

    }
}
