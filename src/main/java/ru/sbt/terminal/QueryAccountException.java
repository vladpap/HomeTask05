package ru.sbt.terminal;

public class QueryAccountException extends AccountException {
    private Account accountException;

    public QueryAccountException(String message) {
        super(message);
    }

    public QueryAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
