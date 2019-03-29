package spbau.xrater.interpretation.instruction;

import org.jetbrains.annotations.NotNull;

/**
 * Factory to create instructions (see {@link Instruction})
 */
public class InstructionFactory {

    /**
     * Creates new assignment instruction
     *
     * @param name name of variable for assignment
     * @param value value of variable to assign
     * @return new instance of assignment instruction
     */
    public static AssignmentInstruction createAssignmentCommand(
        @NotNull final String name,
        @NotNull final String value
    ) {
        return new AssignmentInstruction(name, value);
    }

    /**
     * Creates new empty command instruction
     *
     * @return new instance of command instruction
     */
    public static CommandInstruction createCommandInstruction() {
        return new CommandInstruction();
    }
}
