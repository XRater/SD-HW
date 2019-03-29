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

    @Test
    void testGrepUnitCommandWithoutPatternFlagForError() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep"
        ));
        checkInvalidResult(grepCommand.execute("sometext\n"));
    }

    @Test
    void testGrepUnitCommandWithoutPatternFlag() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "a..b"
        ));
        checkValidResult(grepCommand.execute("axybcd\n"), "axybcd\n");
    }

    @Test
    void testGrepUnitCommandWithAfterContextForError() {
        CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-e", "x", "-A", "-2"
        ));
        checkInvalidResult(grepCommand.execute("sometext\n"));
        grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-e", "x", "-A", "asda"
        ));
        checkInvalidResult(grepCommand.execute("sometext\n"));
    }

    void checkValidResult(final CommandResult result, final String expected) {
        assertTrue(result.isValid());
        assertEquals(expected, result.getResult());
    }

    void checkInvalidResult(final CommandResult result) {
        assertFalse(result.isValid());
    }

    void checkInvalidResult(final CommandResult result, final String message) {
        assertFalse(result.isValid());
        assertEquals(result.getException().getMessage(), message);
    }

    String constructFromLines(final String... lines) {
        return String.join(System.lineSeparator(), lines) + System.lineSeparator();
    }
}
