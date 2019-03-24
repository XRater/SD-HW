package interpretation.commands;

import interpretation.commands.commandResult.CommandResult;
import interpretation.commands.commandUnits.CommandUnit;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Factory class to create commands (see {@link Command})
 */
public class CommandFactory {

    /**
     * @return new command with default implementation
     */
    public static Command createNewCommand() {
        return new DummyCommand();
    }

    private static class DummyCommand implements Command {
        private final Queue<CommandUnit> unitCommandsQueue = new LinkedList<>();

        @Override
        public void addCommandUnit(@NotNull final CommandUnit c) {
            unitCommandsQueue.add(c);
        }

        @Override
        public void run() throws CommandExecutionException {
            if (unitCommandsQueue.isEmpty()) {
                return;
            }
            String commandData = getInitialInput();
            CommandResult commandResult;
            while (!unitCommandsQueue.isEmpty()) {
                final CommandUnit commandUnit = unitCommandsQueue.poll();
                commandResult = commandUnit.execute(commandData);
                if (commandResult.isValid()) {
                    commandData = commandResult.getResult();
                } else {
                    throw new CommandExecutionException(commandResult.getException());
                }
            }
            processOutput(commandData);
        }

        private String getInitialInput() {
            return null;
        }

        private void processOutput(final String output) {
            if (output != null) {
                System.out.println(output);
            }
        }
    }
}
