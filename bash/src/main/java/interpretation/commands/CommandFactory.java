package interpretation.commands;

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
        public void run() {
            if (unitCommandsQueue.isEmpty()) {
                return;
            }
            String commandData = getInitialInput();
            while (!unitCommandsQueue.isEmpty()) {
                final CommandUnit commandUnit = unitCommandsQueue.poll();
                commandData = commandUnit.execute(commandData);
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
