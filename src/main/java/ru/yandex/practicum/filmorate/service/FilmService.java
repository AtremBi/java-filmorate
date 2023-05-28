package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("genreDbStorage") GenreDbStorage genreDbStorage,
                       @Qualifier("mpaDbStorage") MpaDbStorage mpaDbStorage) {
        this.filmStorage = filmStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        for (Film filmDb : filmStorage.getFilms()) {
            films.add(filmBuild(filmDb));
        }
        return films;
    }

    public Film addFilm(Film film) {
        if (isValidation(film)) {
            filmStorage.addFilm(film);
            return filmBuild(film);
        } else {
            throw new ValidationException();
        }
    }

    public Film updateFilm(Film film) {
        if (isValidation(film)) {
            if (filmStorage.checkFilm(film.getId())) {
                genreDbStorage.deleteFilmGenre(film);
                return filmBuild(filmStorage.updateFilm(film));
            } else {
                throw new NotFoundException("Фильм не найден");
            }
        } else {
            throw new ValidationException();
        }
    }

    private Film filmBuild(Film film) {
        Film filmDb = filmStorage.getFilmById(film.getId());
        if (genreDbStorage.getGenreById(film.getId()) != null) {
            genreDbStorage.createFilmGenre(film);
            for (Integer genre : genreDbStorage.getGenreIdByFilm(film)) {
                filmDb.getGenres().add(genreDbStorage.getGenreById(genre));
            }
        }
        filmDb.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()));
        if (filmStorage.getLikesByFilmId(film.getId()) != null) {
            for (Integer like : filmStorage.getLikesByFilmId(film.getId())) {
                filmDb.getLikes().add(like);
            }
        }
        return filmDb;
    }

    public Film getFilmById(int filmId) {
        if (filmStorage.checkFilm(filmId)) {
            return filmBuild(filmStorage.getFilmById(filmId));
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film addLike(int filmId, int userId) {
        if (filmStorage.checkFilm(filmId)) {
            filmStorage.addLike(userId, filmId);
            log.info("Добавлен лайк от - {} фильму - {}", userId, filmId);
            return filmBuild(filmStorage.getFilmById(filmId));
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film deleteLike(int filmId, int userId) {
        if (checkFilmAndLike(filmId, userId)) {
            filmStorage.deleteLike(userId, filmId);
            log.info("Удален лайк - {} у фильму - {}", userId, filmId);
            return filmBuild(filmStorage.getFilmById(filmId));
        } else if (!filmStorage.checkFilm(filmId)) {
            throw new NotFoundException("Фильм не найден");
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public List<Film> getFamousFilms(Integer count) {
        if (count != null) {
            return getFilms().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private boolean checkFilmAndLike(int id, int userId) {
        return filmStorage.checkFilm(id) && filmStorage.checkFilm(userId);
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
