package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.Assert.*;

public class FilmControllerTest {
    private Film film;
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2007, 9, 1))
                .duration(100)
                .build();
    }

    @Test
    public void valid_fields() {
        assertEquals(film.toBuilder().id(1).build(), filmController.postFilm(film));
    }

    @Test
    public void name_cannot_be_empty() {
        assertThrows(NullPointerException.class, () -> film.setName(null));
    }

    @Test
    public void length_description_200_symbols_is_valid(){
        film.setDescription(RandomStringUtils.random(200));
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    public void length_description_201_symbols_is_not_valid(){
        film.setDescription(RandomStringUtils.random(201));
        filmController.postFilm(film);
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void release_date_1895_not_valid(){
        film.setReleaseDate(LocalDate.of(1895, DECEMBER, 28));
        filmController.postFilm(film);
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void release_date_1896_is_valid(){
        film.setReleaseDate(LocalDate.of(1896, DECEMBER, 28));
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }

    @Test
    public void duration_is_negative_not_valid(){
        film.setDuration(-1);
        filmController.postFilm(film);
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void duration_is_negative_is_valid(){
        film.setDuration(1);
        filmController.postFilm(film);
        assertFalse(filmController.getFilms().isEmpty());
    }
}
