package interpretation.commands.commandUnits;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory to create unit command (see {@link CommandUnit})
 */
public class CommandUnitFactory {

    private static final Map<String, Function<List<String>, CommandUnit>> commands = Map.ofEntries(
        Map.entry("echo", CommandUnitFactory::createEchoCommandUnit),
        Map.entry("exit", CommandUnitFactory::createExitCommandUnit),
        Map.entry("pwd", CommandUnitFactory::createPwdCommandUnit),
        Map.entry("cat", CommandUnitFactory::createCatCommandUnit),
        Map.entry("wc", CommandUnitFactory::createWcCommandUnit)
    );

    /**
     * Constructs command from the list of arguments. First token of the list represents
     * command name
     *
     * @param tokens list of arguments. First element represents target unit command name
     * @return constructed command unit
     */
    public static CommandUnit constructCommandUnit(final List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final var constructor = commands.getOrDefault(
                tokens.get(0),
                CommandUnitFactory::createSystemCommandUnit);
        return constructor.apply(tokens);
    }

    private static CommandUnit createEchoCommandUnit(final List<String> args) {
        return new EchoCommandUnit(args);
    }

    private static CommandUnit createExitCommandUnit(final List<String> args) {
        return new ExitCommandUnit(args);
    }

    private static CommandUnit createPwdCommandUnit(final List<String> args) {
        return new PwdCommandUnit(args);
    }

    private static CommandUnit createCatCommandUnit(final List<String> args) {
        return new CatCommandUnit(args);
    }

    private static CommandUnit createWcCommandUnit(final List<String> args) {
        return new WcCommandUnit(args);
    }

    private static CommandUnit createSystemCommandUnit(final List<String> args) {
        return new SystemCommandUnit(args);
    }
}
