package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;
import org.apache.commons.cli.*;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

class GrepCommandUnit implements CommandUnit {

    private static final Options options = new Options();
    private static final CommandLineParser parser = new BasicParser();

    static {
        final Option regex = new Option("e", "regex", true, "pattern");
        regex.setRequired(true);
        options.addOption(regex);
        options.addOption(new Option("i", "ignore-case", false, "ignore case"));
        options.addOption(new Option("w", "word-regexp", false, "match full"));
        options.addOption(new Option("A", "after-context", true, "print more"));
    }

    private final CommandLine cmd;

    GrepCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("grep")) {
            throw new IllegalArgumentException();
        }

        try {
            cmd = parser.parse(options, args.toArray(new String[0]));
            if (cmd.hasOption("after-context")) {
                //noinspection ResultOfMethodCallIgnored
                Integer.parseInt(cmd.getOptionValue("after-context"));
            }
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public CommandResult execute(String input) {
        if (input == null) {
            input = "";
        }
        final Pattern p;
        if (cmd.hasOption("ignore-case")) {
            p = Pattern.compile(cmd.getOptionValue("regex"), Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(cmd.getOptionValue("regex"));
        }
        final int printRange = cmd.hasOption("after-context")
                ? Integer.parseInt(cmd.getOptionValue("after-context")) : 0;
        int lastMatch = -1;
        final StringJoiner joiner = new StringJoiner(System.lineSeparator());
        final String[] lines = input.split(System.lineSeparator());
        for (int i = 0; i < lines.length; i++) {
            if (p.matcher(lines[i]).find()) {
                lastMatch = i;
            }
            if (i - lastMatch <= printRange && lastMatch != -1) {
                joiner.add(lines[i]);
            }
        }
        return CommandResultFactory.createSuccessfulCommandResult(joiner.toString() + '\n');
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof GrepCommandUnit) {
            return cmd.equals(((GrepCommandUnit) obj).cmd);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd);
    }
}
