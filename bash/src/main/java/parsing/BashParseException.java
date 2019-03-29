package parsing;

/**
 * Exception to represent any errors happened during parsing
 */
@SuppressWarnings("WeakerAccess")
public class BashParseException extends Exception {

    public BashParseException(final String message) {
        super(message);
    }

}
