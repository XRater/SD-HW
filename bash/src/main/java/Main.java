import interpretation.Session;
import interpretation.SessionFactory;
import interpretation.commands.CommandExecutionException;
import parsing.BashParseException;

import java.util.Scanner;

/**
 * Main class. Responsible for reading user's input and
 * passing it to the active session for the execution
 */
public class Main {

    public static void main(final String[] args) {
        final Session session = SessionFactory.createNewSession();
        String input;
        final Scanner in = new Scanner(System.in);
        //noinspection InfiniteLoopStatement
        while (true) {
            input = in.nextLine();
            try {
                session.processInput(input);
            } catch (final BashParseException e) {
                System.err.println(e.getMessage());
            } catch (final CommandExecutionException e) {
                String message = "Execution error";
                if (e.getCause().getMessage() != null) {
                    message = e.getCause().getMessage();
                }
                System.err.println(message);
            }
        }
    }

}
