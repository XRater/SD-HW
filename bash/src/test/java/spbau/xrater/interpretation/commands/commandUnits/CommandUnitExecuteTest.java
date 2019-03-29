package spbau.xrater.interpretation.commands.commandUnits;

import org.junit.jupiter.api.Test;
import spbau.xrater.interpretation.commands.commandResult.CommandResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandUnitExecuteTest {

    @Test
    void testCatUnitCommand() {
        final CommandUnit catCommand = CommandUnitFactory.constructCommandUnit(List.of("cat"));
        checkValidResult(catCommand.execute(constructFromLines()), constructFromLines());
        checkValidResult(catCommand.execute(constructFromLines("hello")), constructFromLines("hello"));
        checkValidResult(catCommand.execute(constructFromLines("hello there")), constructFromLines("hello there"));
        checkValidResult(catCommand.execute(constructFromLines("hello", "there")), constructFromLines("hello", "there"));
    }

    @Test
    void testWcUnitCommand() {
        final CommandUnit wcCommand = CommandUnitFactory.constructCommandUnit(List.of("wc"));
        checkValidResult(wcCommand.execute(constructFromLines()), "1 0 1");
        checkValidResult(wcCommand.execute(constructFromLines("hello")), "1 1 6");
        checkValidResult(wcCommand.execute(constructFromLines("a bb")), "1 2 5");
        checkValidResult(wcCommand.execute(constructFromLines("a  bb")), "1 2 6");
        checkValidResult(wcCommand.execute(constructFromLines("a  \tbb")), "1 2 7");
        checkValidResult(wcCommand.execute(constructFromLines("a", "", "b")), "3 2 5");
    }

    @Test
    void testPwdUnitCommand() {
        final CommandUnit pwdCommand = CommandUnitFactory.constructCommandUnit(List.of("pwd"));
        checkValidResult(pwdCommand.execute(""), System.getProperty("user.dir"));
    }

    @Test
    void testEchoUnitCommand() {
        CommandUnit echoCommand;
        echoCommand = CommandUnitFactory.constructCommandUnit(List.of("echo"));
        checkValidResult(echoCommand.execute(""), constructFromLines());
        checkValidResult(echoCommand.execute("hello"), constructFromLines());

        echoCommand = CommandUnitFactory.constructCommandUnit(List.of("echo", "  "));
        checkValidResult(echoCommand.execute(""), constructFromLines("  "));
        checkValidResult(echoCommand.execute("hello"), constructFromLines("  "));

        echoCommand = CommandUnitFactory.constructCommandUnit(List.of("echo", "a", "b"));
        checkValidResult(echoCommand.execute(""), constructFromLines("a b"));
        checkValidResult(echoCommand.execute("hello"), constructFromLines("a b"));
    }

    void checkValidResult(final CommandResult result, final String expected) {
        assertTrue(result.isValid());
        assertEquals(expected, result.getResult());
    }

    void checkValidResult(final CommandResult result, final Class<Exception> clazz) {
        assertFalse(result.isValid());
        assertEquals(result.getException().getClass(), clazz);
    }

    String constructFromLines(final String... lines) {
        return String.join(System.lineSeparator(), lines) + System.lineSeparator();
    }
}
