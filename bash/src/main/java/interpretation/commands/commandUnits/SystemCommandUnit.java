package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        try {
            process = new ProcessBuilder(args).start();
            process.waitFor();
            try (final Reader reader = new InputStreamReader(process.getInputStream())) {
                final char[] buffer = new char[BUF_SIZE];
                while (reader.read(buffer, 0, BUF_SIZE) > 0) {
                    result = result.concat(new String(buffer));
                }
            }
        } catch (final IOException | InterruptedException e) {
            return CommandResultFactory.createUnsuccessfulCommandResult(e);
        }
        return CommandResultFactory.createSuccessfulCommandResult(result);
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
}
