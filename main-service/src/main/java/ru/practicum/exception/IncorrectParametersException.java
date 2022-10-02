package ru.practicum.exception;

public class IncorrectParametersException extends RuntimeException {
    public IncorrectParametersException(String message) {
        super(message);
    }
}
