package com.planning_system.handlers.commands;


public interface CommandParser<T> {
    Command parse(T commandToParse);
}