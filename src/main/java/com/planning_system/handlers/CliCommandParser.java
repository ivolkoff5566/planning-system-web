package com.planning_system.handlers;

import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.commands.CommandParser;
import com.planning_system.handlers.commands.CommandType;
import com.planning_system.handlers.commands.OptionType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.planning_system.handlers.messages.CliMessages.UNRECOGNIZED_COMMAND;


/**
 * Contains methods for parsing {@link Command} from a received String
 */
public class CliCommandParser implements CommandParser<String> {

    /**
     * Method responsible for creating a {@link Command}  object from the input {@link String}
     * @throws RuntimeException if the input String was not parsed successfully</p>
     * @param commandToParse String to parse
     * @return a map with parsed Options
     */
    @Override
    public Command parse(String commandToParse) {
        Command command;

        try {
            String commandTypeString = commandToParse.trim().split(" ")[0];
            command = Command.builder()
                             .type(CommandType.getCommandTypeFromString(commandTypeString))
                             .options(parseOptions(commandToParse))
                             .build();
        } catch (Exception ex) {
            throw new RuntimeException(UNRECOGNIZED_COMMAND);
        }

        return command;
    }

    /**
     * Method responsible for extracting all existing Options from the input {@link String}
     * @param input String to parse
     * @return a map with parsed Options
     */
    private Map<OptionType, String> parseOptions(String input) {
        Pattern pattern = Pattern.compile("-([a-z]{1,2})\\s+(.+?)(?=-[a-z]{1,2}\\s+|$)");
        Matcher matcher = pattern.matcher(input);
        HashMap<OptionType, String> options = new HashMap<>();

        while (matcher.find()) {
            OptionType optionName = OptionType.getOptionTypeFromString(matcher.group(1));
            String optionValue = matcher.group(2).trim();
            options.put(optionName, optionValue);
        }
        return options;
    }
}