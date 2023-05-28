package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

@Component
public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    boolean checkFilm(int id);

    Film getFilmById(int id);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    Set<Integer> getLikesByFilmId(int filmId);
}
