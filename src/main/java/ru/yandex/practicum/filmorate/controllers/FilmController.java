package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.Month.DECEMBER;

@Slf4j
@RestController
public class FilmController {
    private int id;
    Map<Integer, Film> mapFilms = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        try {
            if (!mapFilms.containsKey(film.getId())) {
                id++;
                film.setId(id);
                return validation(film);
            } else {
                throw new ValidException();
            }
        } catch (ValidException e) {
            e.getMessage();
        }
        return null;
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        try {
            if (mapFilms.containsKey(film.getId())) {
                return validation(film);
            } else {
                throw new ValidException();
            }
        } catch (ValidException e) {
            e.getMessage();
        }
        return null;
    }

    private Film validation(Film film) throws ValidException {
        try {
            if (film.getReleaseDate().isAfter(LocalDate.of(1895, DECEMBER, 28)) &&
                    film.getDescription().length() <= 200 &&
                    film.getDuration() > 0) {
                mapFilms.put(film.getId(), film);
                log.info("Фильм обновлен/создан {}", film);
                return mapFilms.get(film.getId());
            } else {
                throw new ValidException();
            }
        } catch (ValidException e) {
            e.getMessage();
        }
        return null;
    }
}
