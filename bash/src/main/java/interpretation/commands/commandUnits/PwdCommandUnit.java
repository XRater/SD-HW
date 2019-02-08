package interpretation.commands.commandUnits;

import java.util.List;
import java.util.StringJoiner;

class PwdCommandUnit implements CommandUnit {

    PwdCommandUnit(final List<String> args) {
        if (args.isEmpty() || !args.get(0).equals("pwd")) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String execute(final String input) {
        return System.getProperty("user.dir");
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof PwdCommandUnit;
    }

}
