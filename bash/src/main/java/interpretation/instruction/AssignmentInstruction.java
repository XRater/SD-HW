package interpretation.instruction;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import interpretation.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Instruction that changes the scope by assigning value of the variable to the target one
 */
public class AssignmentInstruction implements Instruction {

    private final @NotNull String name;
    private final @NotNull String value;

    AssignmentInstruction(final @NotNull String name, final @NotNull String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Sets value of variable
     *
     * @param session session to execute instruction in
     */
    @Override
    public void execute(final @NotNull Session session) {
        session.setVariable(name, value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AssignmentInstruction) {
            final AssignmentInstruction o = (AssignmentInstruction) obj;
            return o.name.equals(name) && o.value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
