package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(int id);

    List<Genre> getAllGenre();

    Set<Genre> getGenreByFilm(Film film);

    void createFilmGenre(Film film);

    void deleteFilmGenre(Film film);

    Map<Integer, Integer> getAllFilmGenre();
}
