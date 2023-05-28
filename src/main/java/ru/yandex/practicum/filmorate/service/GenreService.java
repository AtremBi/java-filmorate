package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(@Qualifier("genreDbStorage") GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenreById(int id) {
        try {
            return genreDbStorage.getGenreById(id);
        } catch (Exception e) {
            throw new NotFoundException("Жанр не найден");
        }
    }

    public List<Genre> getAllGenre() {
        try {
            return genreDbStorage.getAllGenre();
        } catch (Exception e) {
            throw new NotFoundException("Жанры не найдены");
        }
    }

}
