package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static java.util.Calendar.DECEMBER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private Film film;
    private final FilmController filmController;

    @BeforeEach
    public void setUp() {
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2007, 9, 1))
                .duration(100)
                .mpa(Mpa.builder().id(1).build())
                .build();
    }

    @Test
    public void name_cannot_be_empty() {
        assertThrows(NullPointerException.class, () -> film.setName(null));
    }

    @Test
    public void length_description_200_symbols_is_valid() {
        film.setDescription(RandomStringUtils.random(200));
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    public void length_description_201_symbols_is_not_valid() {
        film.setDescription(RandomStringUtils.random(201));
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
    }

    @Test
    public void release_date_1895_not_valid() {
        film.setReleaseDate(LocalDate.of(1895, DECEMBER, 28));
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
    }

    @Test
    public void release_date_1896_is_valid() {
        film.setReleaseDate(LocalDate.of(1896, DECEMBER, 28));
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    public void duration_is_negative_not_valid() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.postFilm(film));
    }

    @Test
    public void duration_is_negative_is_valid() {
        film.setDuration(1);
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }
}
