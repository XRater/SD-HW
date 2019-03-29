package spbau.xrater.interpretation.instruction;

import spbau.xrater.interpretation.Session;
import spbau.xrater.interpretation.commands.CommandExecutionException;
import org.jetbrains.annotations.NotNull;

/**
 * Instruction for interpreter. May be executed in the given {@link Session}
 */
public interface Instruction {

    /**
     * @param session session to execute instruction in
     * @throws CommandExecutionException if exception happened during execution
     */
    void execute(@NotNull Session session) throws CommandExecutionException;

}
