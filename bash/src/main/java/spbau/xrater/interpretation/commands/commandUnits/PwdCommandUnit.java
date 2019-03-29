package spbau.xrater.interpretation.commands.commandUnits;

import spbau.xrater.interpretation.commands.commandResult.CommandResult;
import spbau.xrater.interpretation.commands.commandResult.CommandResultFactory;

import java.util.List;

/**
 * Returns current working directory
 */
class PwdCommandUnit implements CommandUnit {

    PwdCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("pwd")) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CommandResult execute(final String input) {
        return CommandResultFactory.createSuccessfulCommandResult(System.getProperty("user.dir"));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof PwdCommandUnit;
    }

}
