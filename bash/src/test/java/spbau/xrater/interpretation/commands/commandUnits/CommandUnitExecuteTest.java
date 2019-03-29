package spbau.xrater.interpretation.commands.commandUnits;

import org.junit.jupiter.api.Test;
import spbau.xrater.interpretation.commands.commandResult.CommandResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandUnitExecuteTest {

    @Test
    void testCatUnitCommand() {
        final CommandUnit catCommand = CommandUnitFactory.constructCommandUnit(List.of("cat"));
        checkValidResult(catCommand.execute(""), "");
        checkValidResult(catCommand.execute("hello"), "hello");
        checkValidResult(catCommand.execute("hello there"), "hello there");
        checkValidResult(catCommand.execute("hello\nthere"), "hello\nthere");
    }

    @Test
    void testWcUnitCommand() {
        final CommandUnit wcCommand = CommandUnitFactory.constructCommandUnit(List.of("wc"));
        checkValidResult(wcCommand.execute("\n"), "1 0 1");
        checkValidResult(wcCommand.execute("hello\n"), "1 1 6");
        checkValidResult(wcCommand.execute("a bb\n"), "1 2 5");
        checkValidResult(wcCommand.execute("a  bb\n"), "1 2 6");
        checkValidResult(wcCommand.execute("a  \tbb\n"), "1 2 7");
        checkValidResult(wcCommand.execute("a\n\nb\n"), "3 2 5");
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
        checkValidResult(echoCommand.execute(""), "\n");
        checkValidResult(echoCommand.execute("hello"), "\n");

        echoCommand = CommandUnitFactory.constructCommandUnit(List.of("echo", "  "));
        checkValidResult(echoCommand.execute(""), "  \n");
        checkValidResult(echoCommand.execute("hello"), "  \n");

        echoCommand = CommandUnitFactory.constructCommandUnit(List.of("echo", "a", "b"));
        checkValidResult(echoCommand.execute(""), "a b\n");
        checkValidResult(echoCommand.execute("hello"), "a b\n");
    }

    @Test
    void testGrepUnitCommandSimple() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-e", "aaa"
        ));
        checkValidResult(grepCommand.execute(null), "\n");
        checkValidResult(grepCommand.execute("123"), "\n");
        checkValidResult(grepCommand.execute("aaa"), "aaa\n");
        checkValidResult(grepCommand.execute("aaabaaa"), "aaabaaa\n");
        checkValidResult(grepCommand.execute("helloaaa"), "helloaaa\n");
        checkValidResult(grepCommand.execute("first\nhelloaaa\nsecondaaa"), "helloaaa\nsecondaaa\n");
        checkValidResult(grepCommand.execute("first\nhelloaa\nsecondaaa"), "secondaaa\n");
        checkValidResult(grepCommand.execute("first\nhelloAAA\nsecondaaa"), "secondaaa\n");
    }

    @Test
    void testGrepUnitCommandIgnoreCase() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-i", "-e", "a.b"
        ));
        checkValidResult(grepCommand.execute("acb"), "acb\n");
        checkValidResult(grepCommand.execute("ACb"), "ACb\n");
        checkValidResult(grepCommand.execute("xxACbxx"), "xxACbxx\n");
        checkValidResult(grepCommand.execute("xxA.bxx"), "xxA.bxx\n");
    }

    @Test
    void testGrepUnitCommandWFlag() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-w", "-e", "abc"
        ));
        checkValidResult(grepCommand.execute("abc"), "abc\n");
        checkValidResult(grepCommand.execute("abcd"), "\n");
        checkValidResult(grepCommand.execute("yabc"), "\n");
        checkValidResult(grepCommand.execute("y abc d"), "y abc d\n");
    }

    @Test
    void testGrepUnitCommandWith0Context() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-A", "0", "-e", "a"
        ));
        checkValidResult(grepCommand.execute("x\nx\na\nb\na\nc"), "a\na\n");
    }

    @Test
    void testGrepUnitCommandWith2Context() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-A", "2", "-e", "a"
        ));
        checkValidResult(grepCommand.execute("x\nx\na\nb\nc\nd\na\nc"), "a\nb\nc\na\nc\n");
    }

    void checkValidResult(final CommandResult result, final String expected) {
        assertTrue(result.isValid());
        assertEquals(expected, result.getResult());
    }

    void checkValidResult(final CommandResult result, final Class<Exception> clazz) {
        assertFalse(result.isValid());
        assertEquals(result.getException().getClass(), clazz);
    }

}
