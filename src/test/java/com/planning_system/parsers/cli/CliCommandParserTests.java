package com.planning_system.parsers.cli;

import com.planning_system.handlers.CliCommandParser;
import com.planning_system.handlers.commands.Command;
import com.planning_system.handlers.commands.CommandType;
import com.planning_system.handlers.commands.OptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static com.planning_system.handlers.messages.CliMessages.UNRECOGNIZED_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CliCommandParserTests {

    private final CliCommandParser parser = new CliCommandParser();

    @ParameterizedTest
    @MethodSource("getStringCommandTypePairs")
    public void testCommandTypeParsing(String command, CommandType commandType) {
        var expectedCommand = Command.builder()
                                               .type(commandType)
                                               .options(new HashMap<>())
                                               .build();
        assertEquals(expectedCommand, parser.parse(command));
    }

    @Test
    public void testCommandWithOptions() {
        var taskName = "my task";
        var taskDesc = "description";
        var taskStatus = "ready";
        var taskPriority = "high";
        var rejectedTask = "true";
        var taskId = "1";
        var sortBy = "data";
        var reverseSort = "true";
        var commandWithOptions = String.format("create -n %s -d %s -s %s -p %s -rt %s -id %s -st %s -rs %s",
                taskName, taskDesc, taskStatus, taskPriority, rejectedTask, taskId, sortBy, reverseSort);

        HashMap<OptionType, String> expectedOptions = new HashMap<>();
        expectedOptions.put(OptionType.NAME, taskName);
        expectedOptions.put(OptionType.DESCRIPTION, taskDesc);
        expectedOptions.put(OptionType.STATUS, taskStatus);
        expectedOptions.put(OptionType.PRIORITY, taskPriority);
        expectedOptions.put(OptionType.REJECTED_TASK, rejectedTask);
        expectedOptions.put(OptionType.ID, taskId);
        expectedOptions.put(OptionType.SORTED, sortBy);
        expectedOptions.put(OptionType.REVERSE_SORT, reverseSort);

        var expectedCommand = Command.builder()
                .type(CommandType.CREATE)
                .options(expectedOptions)
                .build();
        assertEquals(expectedCommand, parser.parse(commandWithOptions));
    }

    @Test
    public void testInvalidCommandType() {
        var invalidCommand = "invalid_create -n t1";
        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(invalidCommand));
        assertEquals(UNRECOGNIZED_COMMAND, exception.getMessage());
    }

    @Test
    public void testInvalidOptions() {
        var invalidCommand = "create -nn t1";
        Exception exception = assertThrows(RuntimeException.class, () -> parser.parse(invalidCommand));
        assertEquals(UNRECOGNIZED_COMMAND, exception.getMessage());
    }

    private static Stream<Arguments> getStringCommandTypePairs() {
        return Stream.of(
                Arguments.of("create", CommandType.CREATE),
                Arguments.of("get", CommandType.GET),
                Arguments.of("get-all", CommandType.GET_ALL),
                Arguments.of("update", CommandType.UPDATE),
                Arguments.of("delete", CommandType.DELETE),
                Arguments.of("help", CommandType.HELP),
                Arguments.of("exit", CommandType.EXIT)
        );
    }
}