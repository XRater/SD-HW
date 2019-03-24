package interpretation;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents scope with variables for session.
 *
 * By default every variable equals to empty string.
 */
public class Scope {

    private final Map<String, String> variables = new HashMap<>();

    /**
     * Returns value of the variable. If not defined value os equal to empty string
     *
     * @param name target variable name
     * @return value of variable
     */
    public String get(final String name) {
        return variables.getOrDefault(name, "");
    }

    /**
     * Sets value of scope variable
     *
     * @param name name of the target variable
     * @param value target value to set
     */
    public void set(final String name, final String value) {
        variables.put(name, value);
    }
}
