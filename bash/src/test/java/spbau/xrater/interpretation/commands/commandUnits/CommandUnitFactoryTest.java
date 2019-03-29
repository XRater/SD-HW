package spbau.xrater.interpretation.commands.commandUnits;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandUnitFactoryTest {

    @Test
    void testCreateCommand() {
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("echo")),
                new EchoCommandUnit(List.of("echo"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("echo", "1")),
                new EchoCommandUnit(List.of("echo", "1"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("echo", "pwd")),
                new EchoCommandUnit(List.of("echo", "pwd"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("pwd")),
                new PwdCommandUnit(List.of("pwd"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("cat")),
                new CatCommandUnit(List.of("cat"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("cat", "file")),
                new CatCommandUnit(List.of("cat", "file"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("wc")),
                new WcCommandUnit(List.of("wc"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("wc", "file")),
                new WcCommandUnit(List.of("wc", "file"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("exit")),
                new ExitCommandUnit(List.of("exit"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("some")),
                new SystemCommandUnit(List.of("some"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("some", "cat")),
                new SystemCommandUnit(List.of("some", "cat"))
        );
        assertEquals(
                CommandUnitFactory.constructCommandUnit(List.of("some", "cat", "wc")),
                new SystemCommandUnit(List.of("some", "cat", "wc"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> CommandUnitFactory.constructCommandUnit(List.of())
        );
    }

    @Test
    void testIllegalArguments() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CatCommandUnit(List.of("wc", "cat"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CatCommandUnit(List.of("cat", "cat", "hello"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CatCommandUnit(List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PwdCommandUnit(List.of("wc", "cat"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PwdCommandUnit(List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new EchoCommandUnit(List.of("wc", "cat"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new EchoCommandUnit(List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExitCommandUnit(List.of("wc", "cat"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExitCommandUnit(List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new WcCommandUnit(List.of("cat", "cat"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new WcCommandUnit(List.of("wc", "cat", "hello"))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new WcCommandUnit(List.of())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new SystemCommandUnit(List.of())
        );
    }
}