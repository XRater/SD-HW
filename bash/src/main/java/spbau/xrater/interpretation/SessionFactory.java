package spbau.xrater.interpretation;

import spbau.xrater.interpretation.commands.CommandExecutionException;
import spbau.xrater.interpretation.instruction.Instruction;
import org.jetbrains.annotations.NotNull;
import spbau.xrater.parsing.BashParseException;
import spbau.xrater.parsing.BashParser;

import java.util.List;

/**
 * Factory class to create sessions (see {@link Session})
 */
public class SessionFactory {

    /**
     * Creates new Session with empty scope.
     */
    public static Session createNewSession() {
        return new SessionDummy();
    }

    /**
     * Basic implementation of session.
     *
     * Has scope to store variables. Processes input with parsing and
     * calling instructions consequently.
     */
    private static class SessionDummy implements Session {

        private final Scope scope = new Scope();

        @Override
        public void processInput(@NotNull final String input) throws BashParseException, CommandExecutionException {
            final List<String> instructionStrings = BashParser.splitToInstructions(input);
            for (final String instructionString : instructionStrings) {
                final Instruction instruction = BashParser.parse(instructionString, scope);
                instruction.execute(this);
            }
        }

        @Override
        public void setVariable(@NotNull final String name, @NotNull final String value) {
            scope.set(name, value);
        }
    }

}
