package ru.practicum.exception;

public class IncorrectParameters extends RuntimeException {
    public IncorrectParameters(String message) {
        super(message);
    }
}
