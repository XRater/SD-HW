package spbau.xrater.interpretation.instruction;

import org.junit.jupiter.api.Test;
import spbau.xrater.interpretation.Session;
import spbau.xrater.interpretation.SessionFactory;
import spbau.xrater.interpretation.commands.CommandExecutionException;
import spbau.xrater.interpretation.commands.commandUnits.CommandUnitFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InstructionFactoryTest {

    @Test
    void testCreateAssignmentInstruction() {
        Instruction instruction;
        instruction = InstructionFactory.createAssignmentCommand("", "");
        assertEquals(instruction, new AssignmentInstruction("", ""));
        instruction = InstructionFactory.createAssignmentCommand("", "hello");
        assertEquals(instruction, new AssignmentInstruction("", "hello"));
        instruction = InstructionFactory.createAssignmentCommand("a", "");
        assertEquals(instruction, new AssignmentInstruction("a", ""));
        instruction = InstructionFactory.createAssignmentCommand("a", "b");
        assertEquals(instruction, new AssignmentInstruction("a", "b"));
        assertNotEquals(instruction, new CommandInstruction());
    }

    @Test
    void testCreateCommandInstruction() {
        final Instruction instruction = InstructionFactory.createCommandInstruction();
        assertNotEquals(instruction, new CommandInstruction());
        assertNotEquals(instruction, new AssignmentInstruction("", ""));
    }

    @Test
    void testAssignmentInstruction() {
        final Session session = SessionFactory.createNewSession();
        final AssignmentInstruction instruction = InstructionFactory.createAssignmentCommand("a", "b");
        instruction.execute(session);
    }

    @Test
    void testCommandInstruction() throws CommandExecutionException {
        final Session session = SessionFactory.createNewSession();
        final CommandInstruction instruction = InstructionFactory.createCommandInstruction();
        instruction.execute(session);
        instruction.addUnitCommand(CommandUnitFactory.constructCommandUnit(
                List.of("pwd")
        ));
    }
}