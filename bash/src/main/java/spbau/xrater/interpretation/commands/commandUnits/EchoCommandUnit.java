package spbau.xrater.interpretation.commands.commandUnits;

import spbau.xrater.interpretation.commands.commandResult.CommandResult;
import spbau.xrater.interpretation.commands.commandResult.CommandResultFactory;

import java.util.List;
import java.util.Objects;

/**
 * Prints arguments splitted with space character
 */
class EchoCommandUnit implements CommandUnit {

    private final List<String> args;

    EchoCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("echo")) {
            throw new IllegalArgumentException();
        }
        this.args = args.subList(1, args.size());
    }

    @Override
    public CommandResult execute(final String input) {
        return CommandResultFactory.createSuccessfulCommandResult(String.join(" ", args) + System.lineSeparator());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EchoCommandUnit) {
            return Objects.equals(((EchoCommandUnit) obj).args, args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(args);
    }
}
