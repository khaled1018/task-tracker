package org.nti.tasktracker.exceptions;

public class MaxTasksExceededException extends RuntimeException {
    public MaxTasksExceededException(String s) {
        super(s);
    }
}
