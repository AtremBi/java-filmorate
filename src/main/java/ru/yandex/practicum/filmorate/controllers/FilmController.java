package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        if (filmStorage.getFilms().containsKey(filmId)) {
            return filmStorage.getFilms().get(filmId);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilm(@RequestParam(name = "count", defaultValue = "10") int count) {
        return filmService.getFamousFilms(count);
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        if (filmStorage.getFilms().containsKey(id) && filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            return filmService.addLike(id, userId);
        } else if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (filmStorage.getFilms().containsKey(id) && filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            return filmService.deleteLike(id, userId);
        } else if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
