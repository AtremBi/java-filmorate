package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Component
public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Map<Integer, Film> getFilms();
}
