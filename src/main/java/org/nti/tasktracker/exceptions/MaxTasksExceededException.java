package org.nti.tasktracker;

public class MaxTasksExceededException extends RuntimeException {
    public MaxTasksExceededException(String s) {
        super(s);
    }
}
