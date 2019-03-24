package interpretation.commands.commandUnits;

import java.util.List;
import java.util.Objects;

class EchoCommandUnit implements CommandUnit {

    private final List<String> args;

    EchoCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("echo")) {
            throw new IllegalArgumentException();
        }
        this.args = args.subList(1, args.size());
    }

    @Override
    public String execute(final String input) {
        return String.join(" ", args);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EchoCommandUnit) {
            return Objects.equals(((EchoCommandUnit) obj).args, args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(args);
    }
}
