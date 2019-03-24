package interpretation.commands;

import interpretation.commands.commandUnits.CommandUnit;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface Command {

    void addCommandUnit(@NotNull CommandUnit c);

    void run() throws CommandExecutionException;
}
