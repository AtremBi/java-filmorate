package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.Month.DECEMBER;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private final Map<Integer, Film> mapFilms = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (isValidation(film)) {
            if (!mapFilms.containsKey(film.getId())) {
                id++;
                film.setId(id);
                mapFilms.put(film.getId(), film);
                log.info("Фильм обновлен/создан {}", film);
                return mapFilms.get(film.getId());
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (isValidation(film)) {
            if (mapFilms.containsKey(film.getId())) {
                mapFilms.put(film.getId(), film);
                return mapFilms.get(film.getId());
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return mapFilms;
    }

    private boolean isValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isAfter(LocalDate.of(1895, DECEMBER, 28)) &&
                film.getDescription().length() <= 200 &&
                film.getDuration() > 0) {
            return true;
        } else {
            throw new ValidationException();
        }
    }
}
