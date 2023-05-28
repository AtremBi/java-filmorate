package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenreById(int id);

    List<Genre> getAllGenre();

    List<Integer> getGenreIdByFilm(Film film);

    void createFilmGenre(Film film);

    void deleteFilmGenre(Film film);
}
