package interpretation.commands.commandUnits;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

class SystemCommandUnit implements CommandUnit {

    private final List<String> args;

    SystemCommandUnit(final List<String> args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.args = args.subList(0, args.size());
    }

    @Override
    public String execute(final String input) {
        final Process p;
        try {
            p = new ProcessBuilder(args).start();
            p.waitFor();
        } catch (final IOException | InterruptedException e) {
            System.err.println("Failed to execute command '" + args.get(0) + "'");
        }
        return null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SystemCommandUnit) {
            return Objects.equals(((SystemCommandUnit) obj).args, args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(args);
    }
}
