package ru.yandex.practicum.filmorate.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidException extends IllegalArgumentException {

    public ValidException() {
        super("Не пройдена валидация");
        log.error("Не пройдена валидация");
    }
}
