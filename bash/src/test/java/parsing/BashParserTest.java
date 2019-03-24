package parsing;

import interpretation.Scope;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BashParserTest {

    @Test
    void testApplyScope() throws BashParseException {
        final Scope scope = new Scope();
        scope.set("a", "1");
        scope.set("aa", "2");
        scope.set("aka", "3");
        assertEquals(BashParser.TestBashParser.callApplyScope("", scope), "");
        assertEquals(BashParser.TestBashParser.callApplyScope("a", scope), "a");
        assertEquals(BashParser.TestBashParser.callApplyScope("$a", scope), "1");
        assertEquals(BashParser.TestBashParser.callApplyScope("$aa", scope), "2");
        assertEquals(BashParser.TestBashParser.callApplyScope("$aka", scope), "3");
        assertEquals(BashParser.TestBashParser.callApplyScope("$a$aa", scope), "12");
        assertEquals(BashParser.TestBashParser.callApplyScope("$a$a", scope), "11");
        assertEquals(BashParser.TestBashParser.callApplyScope("$a$ka", scope), "1");
        assertEquals(BashParser.TestBashParser.callApplyScope("$aka\"\"", scope), "3");
        assertEquals(BashParser.TestBashParser.callApplyScope("$aka\"\"$a", scope), "31");
        assertEquals(BashParser.TestBashParser.callApplyScope("a$a\'\'a", scope), "a1a");

        assertEquals(BashParser.TestBashParser.callApplyScope("\"$a\"", scope), "1");
        assertEquals(BashParser.TestBashParser.callApplyScope("\"$aa\"", scope), "2");
        assertEquals(BashParser.TestBashParser.callApplyScope("\"$a\"\"a\"", scope), "1a");
        assertEquals(BashParser.TestBashParser.callApplyScope("\"'$a'\"", scope), "'1'");

        assertEquals(BashParser.TestBashParser.callApplyScope("'$a'", scope), "$a");
        assertEquals(BashParser.TestBashParser.callApplyScope("'$a$a'", scope), "$a$a");
        assertEquals(BashParser.TestBashParser.callApplyScope("'$a'$a'$a'", scope), "$a1$a");
        assertEquals(BashParser.TestBashParser.callApplyScope("'$a\"$a\"$a'", scope), "$a\"$a\"$a");

        assertThrows(BashParseException.class, () -> BashParser.TestBashParser.callApplyScope("'", scope));
        assertThrows(BashParseException.class, () -> BashParser.TestBashParser.callApplyScope("\"", scope));
        assertThrows(BashParseException.class, () -> BashParser.TestBashParser.callApplyScope("'$a", scope));
    }

    @Test
    void testCheckInstructionForAssignment() {
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a=2"));
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a=2 4"));
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a=$b 4"));
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a=44"));
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a='44'"));
        assertTrue(BashParser.TestBashParser.callCheckInstructionForAssignment("a=\"44\""));

        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("ass"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("=44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("'a=44'"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("a''=44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("$a=44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("a\'=\'44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("a\"=\"44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("\"=\"44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("\'=\'44"));
        assertFalse(BashParser.TestBashParser.callCheckInstructionForAssignment("a$b=44"));
    }

    @Test
    void testCallSplitTokensIgnoringQuotes() {
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("", ' '),
                List.of()
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("a", ' '),
                List.of("a")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("a b", ' '),
                List.of("a", "b")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("'a'", ' '),
                List.of("'a'")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("\"a\"", ' '),
                List.of("\"a\"")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("'a b'", ' '),
                List.of("'a b'")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitTokensIgnoringQuotes("'a' 'b'", ' '),
                List.of("'a'", "'b'")
        );
    }

    @Test
    void testCallSplitToUnitCommands() {
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands(""),
                List.of()
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("echo 1"),
                List.of("echo 1")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("echo 1 | echo 2"),
                List.of("echo 1 ", " echo 2")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("echo 1|echo 2|pwd"),
                List.of("echo 1", "echo 2", "pwd")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("\"echo\"|"),
                List.of("\"echo\"")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("'echo 1 | echo 2'"),
                List.of("'echo 1 | echo 2'")
        );
        assertEquals(
                BashParser.TestBashParser.callSplitToUnitCommands("echo||echo"),
                List.of("echo", "echo")
        );
    }
}