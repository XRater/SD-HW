package interpretation.commands.commandUnits;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandResult.CommandResultFactory;

import java.util.List;

class ExitCommandUnit implements CommandUnit {

    ExitCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("exit")) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CommandResult execute(final String input) {
        System.exit(0);
        return CommandResultFactory.createSuccessfulCommandResult(null);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ExitCommandUnit;
    }

}
