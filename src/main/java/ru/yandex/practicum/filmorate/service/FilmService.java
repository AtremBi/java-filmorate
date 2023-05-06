package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage){
        this.storage = storage;
    }

    public Film addLike(int filmId, int userId){
        storage.getFilms().get(filmId).getLikes().add(userId);
        log.info("Добавлен лайк от - {} фильму - {}", userId, filmId);
        return storage.getFilms().get(filmId);
    }

    public Film deleteLike(int filmId, int userId){
        storage.getFilms().get(filmId).getLikes().remove(userId);
        log.info("Удален лайк - {} у фильму - {}", userId, filmId);
        return storage.getFilms().get(filmId);
    }

    public List<Film> getFamousFilms(Integer count){
        if (count != null){
            return storage.getFilms().values().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
