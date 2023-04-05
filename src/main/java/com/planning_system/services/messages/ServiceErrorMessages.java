package com.planning_system.services.messages;

public class ServiceErrorMessages {
    public static final String ERROR = "Something went wrong. Please try again.";
    public static final String TASK_NOT_FOUND = "Could not find a task with provided id.";
    public static final String ID_NOT_NUMBER = "The id must be a valid number starting from 1";
    public static final String INVALID_STATUS = "Invalid task status. Possible statuses are: [todo, ready, in_progress, completed]";
    public static final String INVALID_PRIORITY = "Invalid task priority. Possible priority: [high, low]";
    public static final String INVALID_SORTING_TYPE = "Invalid sorting type. You can sort Tasks by [date, status]";
    public static final String NO_FIELDS_TO_UPDATE = "You didn't enter any fields that can be updated.";
    public static final String NO_NAME_ENTERED = "You must enter name of the task.";
}