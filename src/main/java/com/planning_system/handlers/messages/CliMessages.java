package com.planning_system.handlers.messages;

import static com.planning_system.handlers.commands.CommandType.CREATE;
import static com.planning_system.handlers.commands.CommandType.DELETE;
import static com.planning_system.handlers.commands.CommandType.EXIT;
import static com.planning_system.handlers.commands.CommandType.GET;
import static com.planning_system.handlers.commands.CommandType.GET_ALL;
import static com.planning_system.handlers.commands.CommandType.HELP;
import static com.planning_system.handlers.commands.CommandType.UPDATE;

public class CliMessages {
    public static final String WELCOME = "Welcome to the Planning System! \nEnter [help] if you need more info.";
    public static final String NEW_LINE = "> ";
    public static final String INFO = "Available commands and [options] are: " +
            "\n" + CREATE.getTypeName() + "[-n, -d, -p, -s]" +
            "\n" + GET.getTypeName() + "[-id]" +
            "\n" + GET_ALL.getTypeName() + "[-st, -rt]" +
            "\n" + UPDATE.getTypeName() + "[-id, -d, -s]" +
            "\n" + DELETE.getTypeName() + "[-id]" +
            "\n" + HELP.getTypeName()  +
            "\n" + EXIT.getTypeName();
    public static final String BYE = "Bye!";

    // ERRORS
    public static final String ERROR = "Something went wrong. Please try again.";
    public static final String UNRECOGNIZED_COMMAND = "Your command was not recognized. " +
            "Type \"help\" to see all the available commands." +
            "\nEnter \"exit\" to close the program.";
}