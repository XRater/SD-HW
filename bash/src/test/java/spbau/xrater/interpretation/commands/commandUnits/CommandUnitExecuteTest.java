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
        checkValidResult(grepCommand.execute(null), constructFromLines());
        checkValidResult(grepCommand.execute("123"), constructFromLines());
        checkValidResult(grepCommand.execute("aaa"), constructFromLines("aaa"));
        checkValidResult(grepCommand.execute("aaabaaa"), constructFromLines("aaabaaa"));
        checkValidResult(grepCommand.execute("helloaaa"), constructFromLines("helloaaa"));
        checkValidResult(grepCommand.execute(constructFromLines("first", "helloaaa", "secondaaa")), constructFromLines("helloaaa", "secondaaa"));
        checkValidResult(grepCommand.execute(constructFromLines("first", "helloaa", "secondaaa")), constructFromLines("secondaaa"));
        checkValidResult(grepCommand.execute(constructFromLines("first", "helloAAA", "secondaaa")), constructFromLines("secondaaa"));
    }

    @Test
    void testGrepUnitCommandIgnoreCase() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-i", "-e", "a.b"
        ));
        checkValidResult(grepCommand.execute("acb"), constructFromLines("acb"));
        checkValidResult(grepCommand.execute("ACb"), constructFromLines("ACb"));
        checkValidResult(grepCommand.execute("xxACbxx"), constructFromLines("xxACbxx"));
        checkValidResult(grepCommand.execute("xxA.bxx"), constructFromLines("xxA.bxx"));
    }

    @Test
    void testGrepUnitCommandWFlag() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-w", "-e", "abc"
        ));
        checkValidResult(grepCommand.execute("abc"), constructFromLines("abc"));
        checkValidResult(grepCommand.execute("abcd"), constructFromLines());
        checkValidResult(grepCommand.execute("yabc"), constructFromLines());
        checkValidResult(grepCommand.execute("y abc d"), constructFromLines("y abc d"));
    }

    @Test
    void testGrepUnitCommandWith0Context() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-A", "0", "-e", "a"
        ));
        checkValidResult(grepCommand.execute(constructFromLines("x", "x","a", "b", "a", "c")), constructFromLines("a", "a"));
    }

    @Test
    void testGrepUnitCommandWith2Context() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-A", "2", "-e", "a"
        ));
        checkValidResult(
                grepCommand.execute(constructFromLines("x", "x", "a", "b", "c", "d", "a", "c")),
                constructFromLines("a", "b", "c", "a", "c")
        );
    }

    @Test
    void testGrepUnitCommandWithoutPatternFlagForError() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep"
        ));
        checkInvalidResult(grepCommand.execute(constructFromLines("sometext")));
    }

    @Test
    void testGrepUnitCommandWithoutPatternFlag() {
        final CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "a..b"
        ));
        checkValidResult(grepCommand.execute(constructFromLines("axybcd")), constructFromLines("axybcd"));
    }

    @Test
    void testGrepUnitCommandWithAfterContextForError() {
        CommandUnit grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-e", "x", "-A", "-2"
        ));
        checkInvalidResult(grepCommand.execute(constructFromLines("sometext")));
        grepCommand = CommandUnitFactory.constructCommandUnit(List.of(
                "grep", "-e", "x", "-A", "asda"
        ));
        checkInvalidResult(grepCommand.execute(constructFromLines("sometext")));
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
