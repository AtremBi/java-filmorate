package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private final Map<Integer, Film> mapFilms = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        id++;
        film.setId(id);
        mapFilms.put(film.getId(), film);
        log.info("Фильм обновлен/создан {}", film);
        return mapFilms.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        mapFilms.put(film.getId(), film);
        return mapFilms.get(film.getId());
    }

    @Override
    public Film getFilmById(int filmId) {
        return mapFilms.get(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    @Override
    public boolean checkFilm(int id) {
        return mapFilms.containsKey(id);
    }
}
