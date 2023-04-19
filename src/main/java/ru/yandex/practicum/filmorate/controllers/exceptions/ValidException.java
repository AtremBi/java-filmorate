package ru.yandex.practicum.filmorate.controllers.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        log.error("Не пройдена валидация");
        return "Не пройдена валидация";
    }
}
