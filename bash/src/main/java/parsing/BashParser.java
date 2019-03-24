package parsing;

import interpretation.Scope;
import interpretation.commands.commandUnits.CommandUnit;
import interpretation.commands.commandUnits.CommandUnitFactory;
import interpretation.instruction.AssignmentInstruction;
import interpretation.instruction.CommandInstruction;
import interpretation.instruction.Instruction;
import interpretation.instruction.InstructionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class with static parsing methods.
 */
public class BashParser {

    private BashParser() {}

    /**
     * Splits users's input to instructions.
     *
     * Base implementation represents full string as one instruction.
     *
     * @param input string to process
     * @return list of strings with instructions
     */
    @NotNull
    public static List<String> splitToInstructions(final @NotNull String input) {
        return Collections.singletonList(input);
    }

    @NotNull
    public static Instruction parse(
             @NotNull final String input,
             @NotNull final Scope scope
    ) throws BashParseException {
        final List<String> unitCommands = splitToUnitCommands(input);
        if (unitCommands.isEmpty()) {
            return InstructionFactory.createCommandInstruction();
        }
        if (checkInstructionForAssignment(unitCommands.get(0))) {
            if (unitCommands.size() == 1) {
                return parseToAssignment(input, scope);
            } else {
                throw new BashParseException("Chaining assignment command is forbidden");
            }
        } else {
            final CommandInstruction commandInstruction = InstructionFactory.createCommandInstruction();
            for (final var unitCommand : unitCommands) {
                commandInstruction.addUnitCommand(parseToUnitCommand(unitCommand, scope));
            }
            return commandInstruction;
        }
    }

    @NotNull
    private static List<String> splitToUnitCommands(final @NotNull String input) {
        return splitToTokensIgnoringQuotes(input, '|');
    }

    @NotNull
    private static CommandUnit parseToUnitCommand(final @NotNull String unitCommand, final Scope scope) throws BashParseException {
        final List<String> unparsedTokens = splitToTokensIgnoringQuotes(unitCommand, ' ');
        if (unparsedTokens.isEmpty()) {
            throw new BashParseException("Empty command is not supported");
        }
        final List<String> tokens = new ArrayList<>();
        for (final var unparsedToken : unparsedTokens) {
            tokens.add(applyScope(unparsedToken, scope));
        }
        return CommandUnitFactory.constructCommandUnit(tokens);
    }

    private static boolean checkInstructionForAssignment(final @NotNull String input) {
        return input.matches("^[^\'\"=$][^\'\"$]*=.*$");
    }

    @NotNull
    private static AssignmentInstruction parseToAssignment(final String input, final @NotNull Scope scope) throws BashParseException {
        final int index = input.indexOf('=');
        final String name = input.substring(0, index);
        final String value = applyScope(input.substring(index + 1), scope);
        return InstructionFactory.createAssignmentCommand(name, value);
    }

    /**
     * The methods evaluates input in given scope (replaces variables with their values).
     *
     * @param input string to process
     * @param scope scope to evaluate input with
     * @return string representation of input evaluated in the given scope
     * @throws BashParseException if input string was not correct
     * (for example had unpaired quotes)
     */
    @NotNull
    private static String applyScope(final @NotNull String input, final @NotNull Scope scope) throws BashParseException {
        final StringBuilder stringBuilder = new StringBuilder();
        Character quote = null;
        boolean inVariable = false;
        final StringBuilder variable = new StringBuilder();
        for (final var c : input.toCharArray()) {
            if (inVariable) {
                if (c == '\'' || c == '\"' || c == ' ' || c == '$') {
                    stringBuilder.append(scope.get(variable.toString()));
                    variable.setLength(0);
                    inVariable = false;
                } else {
                    variable.append(c);
                }
            }
            if (c == '$') {
                if (quote == null || quote == '\"') {
                    inVariable = true;
                } else {
                    stringBuilder.append(c);
                }
            } else if (c == '\'' || c == '\"') {
                if (quote == null) {
                    quote = c;
                } else {
                    if (quote == c) {
                        quote = null;
                    } else {
                        stringBuilder.append(c);
                    }
                }
            } else if (!inVariable) {
                stringBuilder.append(c);
            }
        }
        if (inVariable) {
            stringBuilder.append(scope.get(variable.toString()));
        }
        if (quote != null) {
            throw new BashParseException("Unpaired quote was found");
        }
        return stringBuilder.toString();
    }

    /**
     * The method splits input string by symbol, ignoring its occurrences
     * inside the quotes.
     *
     * @param input input to process
     * @param symbol symbol to split with
     * @return list of string tokens after splitting
     */
    @NotNull
    private static List<String> splitToTokensIgnoringQuotes(@NotNull final String input, final char symbol) {
        final List<String> tokens = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();
        Character quote = null;
        for (final var c : input.toCharArray()) {
            if (quote == null) {
                if (c == '\'' || c == '\"') {
                    quote = c;
                } else if (c == symbol) {
                    if (sb.length() != 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                    continue;
                }
            } else if (c == quote) {
                quote = null;
            }
            sb.append(c);
        }
        if (sb.length() != 0) {
            tokens.add(sb.toString());
        }
        return tokens;
    }

    /* package private class for testing private methods of BashParser */
    static class TestBashParser {

        @SuppressWarnings("SameParameterValue")
        static List<String> callSplitTokensIgnoringQuotes(final String input, final char symbol) {
            return splitToTokensIgnoringQuotes(input, symbol);
        }

        static List<String> callSplitToUnitCommands(final String input) {
            return splitToUnitCommands(input);
        }

        static String callApplyScope(final String input, final Scope scope) throws BashParseException {
            return applyScope(input, scope);
        }

        static boolean callCheckInstructionForAssignment(final String input) {
            return checkInstructionForAssignment(input);
        }
    }
}
