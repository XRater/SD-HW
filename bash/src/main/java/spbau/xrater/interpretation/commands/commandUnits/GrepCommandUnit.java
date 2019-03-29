package spbau.xrater.interpretation.commands.commandUnits;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import spbau.xrater.interpretation.commands.commandResult.CommandResult;
import spbau.xrater.interpretation.commands.commandResult.CommandResultFactory;

import java.io.File;
import java.io.IOException;
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
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public CommandResult execute(String input) {
        if (input == null) {
            input = "";
        }
        if (cmd.getArgs().length > 1) {
            if (cmd.getArgs().length > 2) {
                return CommandResultFactory.createUnsuccessfulCommandResult(
                    new Exception("Too much arguments")
                );
            }
            try {
                input =  FileUtils.readFileToString(new File(cmd.getArgs()[1]), (String) null);
            } catch (final IOException e) {
                return CommandResultFactory.createUnsuccessfulCommandResult(e);
            }
        }
        if (cmd.hasOption("after-context")) {
            try {
                Integer.parseInt(cmd.getOptionValue("after-context"));
            } catch (final NumberFormatException e) {
                return CommandResultFactory.createUnsuccessfulCommandResult(e);
            }
        }
        String pattern = cmd.getOptionValue("regex");
        if (cmd.hasOption("word-regexp")) {
           pattern = "\\b" + pattern + "\\b";
        }
        final Pattern p;
        if (cmd.hasOption("ignore-case")) {
            p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(pattern);
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
