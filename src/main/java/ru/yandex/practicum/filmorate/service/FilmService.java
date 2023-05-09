package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film addFilm(Film film) {
        if (isValidation(film)) {
            return storage.addFilm(film);
        } else {
            throw new ValidationException();
        }
    }

    public Film updateFilm(Film film) {
        if (isValidation(film)) {
            if (storage.checkFilm(film.getId())) {
                return storage.updateFilm(film);
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }

    public Film getFilmById(int filmId) {
        if (storage.checkFilm(filmId)) {
            return storage.getFilmById(filmId);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film addLike(int filmId, int userId) {
        if (storage.checkFilm(filmId)) {
            storage.getFilmById(filmId).getLikes().add(userId);
            log.info("Добавлен лайк от - {} фильму - {}", userId, filmId);
            return storage.getFilmById(filmId);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film deleteLike(int filmId, int userId) {
        if (checkFilmAndLike(filmId, userId)) {
            storage.getFilmById(filmId).getLikes().remove(userId);
            log.info("Удален лайк - {} у фильму - {}", userId, filmId);
            return storage.getFilmById(filmId);
        } else if (!storage.checkFilm(filmId)) {
            throw new NotFoundException("Фильм не найден");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public List<Film> getFamousFilms(Integer count) {
        if (count != null) {
            return storage.getFilms().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private boolean checkFilmAndLike(int id, int userId) {
        return storage.checkFilm(id) && storage.checkFilm(userId);
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
