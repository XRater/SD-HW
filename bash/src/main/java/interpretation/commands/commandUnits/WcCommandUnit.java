package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
* The command returns number of bytes, words and lines in file.
* Works only for small files (with no more then 2^31 bytes)
* */
class WcCommandUnit implements CommandUnit {

    private final String file;

    WcCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("wc")) {
            throw new IllegalArgumentException();
        }
        if (args.size() > 2) {
            throw new IllegalArgumentException();
        }
        this.file = args.size() == 2 ? args.get(1) : null;
    }

    // CommandUnit classes are implementations of different commands, therefore
    // we dont want to have more connections between pair of them (only common interface)
    @SuppressWarnings("Duplicates")
    @Override
    public CommandResult execute(final String input) {
        final String content;
        if (file != null) {
            final File targetFile = new File(file);
            try {
                content =  FileUtils.readFileToString(targetFile, (String) null);
            } catch (final IOException e) {
                return CommandResultFactory.createUnsuccessfulCommandResult(e);
            }
        } else {
            content = input == null ? "" : input;
        }
        long linesNumber = Arrays.stream(content.split(System.lineSeparator())).count();
        if (linesNumber == 0) {
            linesNumber = 1;
        }
        final long wordsNumber = Arrays.stream(content.split("\\s"))
                .filter( word -> !word.isEmpty() ).count();
        final long bytesNumber = content.length();
        return CommandResultFactory.createSuccessfulCommandResult(
                linesNumber + " " + wordsNumber + " " + bytesNumber
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof WcCommandUnit) {
            return Objects.equals(((WcCommandUnit) obj).file, file);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
