package ru.practicum.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.ExceptionDto;
import ru.practicum.exception.IncorrectParametersException;
import ru.practicum.exception.NoAccessException;
import ru.practicum.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(IncorrectParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto onBadRequest(IncorrectParametersException e) {
        return new ExceptionDto(null, "неверные параметры запроса", e.getMessage(), "BAD_REQUEST", LocalDateTime.now());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto onNotFound(IncorrectParametersException e) {
        return new ExceptionDto(null, "Объект не найден", e.getMessage(), "NOT_FOUND", LocalDateTime.now());
    }

    @ResponseBody
    @ExceptionHandler(NoAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto onForbidden(IncorrectParametersException e) {
        return new ExceptionDto(null, "отказано в доступе", e.getMessage(), "FORBIDDEN", LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDto onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ExceptionDto(errors, "Данные не прошли валидацию", e.getMessage(), "BAD_REQUEST", LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDto onDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ExceptionDto(null, "Ошибка целостности данных", e.getMessage(), "BAD_REQUEST", LocalDateTime.now());
    }

}
