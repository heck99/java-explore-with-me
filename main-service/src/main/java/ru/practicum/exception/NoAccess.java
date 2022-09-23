package ru.practicum.exception;

public class NoAccess extends RuntimeException {
    public NoAccess(String message) {
        super(message);
    }
}
