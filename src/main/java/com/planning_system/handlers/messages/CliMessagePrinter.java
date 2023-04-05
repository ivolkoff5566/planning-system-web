package com.planning_system.handlers.messages;

import com.planning_system.handlers.response.Response;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.planning_system.handlers.messages.CliMessages.NEW_LINE;

public class CliMessagePrinter {

    public void print(String message) {
        System.out.println(message);
    }

    public void print(Response<?> response) {
        print(response.getMessage());
        if (Objects.nonNull(response.getData())) {
            if (response.getData() instanceof Collection<?>) {
                List<?> list = (List<?>) response.getData();
                list.forEach(System.out::println);
            } else {
                System.out.println(response.getData());
            }
        }
    }

    public void printNewLineSymbol() {
        System.out.print(NEW_LINE);
    }
}