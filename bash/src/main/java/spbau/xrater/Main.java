package spbau.xrater;

import spbau.xrater.interpretation.Session;
import spbau.xrater.interpretation.SessionFactory;
import spbau.xrater.interpretation.commands.CommandExecutionException;
import spbau.xrater.parsing.BashParseException;

import java.util.Scanner;

/**
 * spbau.xrater.Main class. Responsible for reading user's input and
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
                if (e.getMessage() != null) {
                    System.err.println(e.getMessage());
                } else {
                    System.err.println("Unknown parsing error");
                }
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
