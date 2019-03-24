package interpretation.instruction;

import interpretation.Session;
import org.jetbrains.annotations.NotNull;

/**
 * Instruction for interpreter. May be executed in the given {@link Session}
 */
public interface Instruction {

    /**
     * @param session session to execute instruction in
     */
    void execute(@NotNull Session session);

}
