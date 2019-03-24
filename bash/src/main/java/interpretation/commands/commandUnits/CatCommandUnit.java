package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

class CatCommandUnit implements CommandUnit {

    private final String file;

    CatCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("cat")) {
            throw new IllegalArgumentException();
        }
        if (args.size() > 2) {
            throw new IllegalArgumentException();
        }
        this.file = args.size() == 2 ? args.get(1) : null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public CommandResult execute(final String input) {
        final String actualFile = input == null ? file : input;
        if (actualFile == null) {
            throw new IllegalArgumentException();
        }
        final File file = new File(actualFile);
        try {
            return CommandResultFactory.createSuccessfulCommandResult(
                    FileUtils.readFileToString(file, (String) null)
            );
        } catch (final IOException e) {
            return CommandResultFactory.createUnsuccessfulCommandResult(e);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CatCommandUnit) {
            return Objects.equals(((CatCommandUnit) obj).file, file);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
