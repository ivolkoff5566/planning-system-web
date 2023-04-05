package com.planning_system.handlers;

import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.commands.CommandParser;
import com.planning_system.handlers.executors.CliCommandExecutor;
import com.planning_system.handlers.messages.CliMessagePrinter;
import com.planning_system.handlers.messages.CliMessages;

import java.util.Scanner;

/**
 * The class mainly responsible for user interaction.
 * Contains methods for reading input from users, processing commands and printing results.
 */
public class CliCommandHandler implements CommandHandler {

    private final CliMessagePrinter messagePrinter;
    private final CommandParser<String> commandParser;
    private final CliCommandExecutor cliCommandExecutor;

    public CliCommandHandler(final CliMessagePrinter cliMessagePrinter,
                             final CommandParser<String> commandParser,
                             final CliCommandExecutor cliCommandExecutor) {
        this.messagePrinter = cliMessagePrinter;
        this.commandParser = commandParser;
        this.cliCommandExecutor = cliCommandExecutor;
    }

    /**
     * The method serves as an entry point to start the application.
     * Initialize Scanner object that will be used for reading input from the user.
     */
    @Override
    public void start() {
        messagePrinter.print(CliMessages.WELCOME);
        try (Scanner scanner = new Scanner(System.in)) {
            processUserInput(scanner);
        } catch (Exception ex) {
            messagePrinter.print(ex.getMessage());
        }
    }

    /**
     * The method responsible for reading lines (Strings) from the console and processing received commands
     * (parse, execute, print result or error).
     * @param scanner Scanner that will be used to read line form the console
     */
    public void processUserInput(Scanner scanner) {
        messagePrinter.printNewLineSymbol();
        String input = scanner.nextLine();
        try {
            Command command = commandParser.parse(input);
            cliCommandExecutor.executeCommand(command).ifPresent(messagePrinter::print);
        } catch (Exception ex) {
            messagePrinter.print(ex.getMessage());
        }
        processUserInput(scanner);
    }
}