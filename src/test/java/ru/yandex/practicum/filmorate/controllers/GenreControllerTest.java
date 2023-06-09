package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreControllerTest {
    private final GenreController genreController;

    @Test
    public void getNotEmptyListGenres_callMethodGetAllGenre() {
        Assertions.assertFalse(genreController.getAllGenre().isEmpty());
    }

    @Test
    public void getGenreById_callMethodGetGenreById() {
        Assertions.assertEquals(Genre.builder().id(2).name("Драма").build(), genreController.getGenreById(2));
    }

    @Test
    public void getNotFoundException_callMethodGetGenreById() {
        Assertions.assertThrows(NotFoundException.class, () -> genreController.getGenreById(9));
    }
}
