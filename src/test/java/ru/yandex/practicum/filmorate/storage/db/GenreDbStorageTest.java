package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    public void getAllGenre_get_not_empty_list_genres_from_db() {
        Assertions.assertFalse(genreDbStorage.getAllGenre().isEmpty());
    }

    @Test
    public void getGenreById_get_genre_by_id_from_db() {
        Assertions.assertEquals(Genre.builder().id(2).name("Драма").build(), genreDbStorage.getGenreById(2));
    }

    @Test
    public void getNotFoundException_get_exception_by_search_genre_with_unknown_id() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> genreDbStorage.getGenreById(9));
    }
}
