package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;


@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private Film film;
    private User user;

    @BeforeEach
    public void setUp() {
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(1999, 8, 17))
                .duration(136)
                .build();
        film.setGenres(new HashSet<>());
        film.setLikes(new HashSet<>());
        film.setMpa(Mpa.builder()
                .id(1)
                .name("NC-17")
                .build());

        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .birthday(LocalDate.of(1999, 8, 17))
                .build();
        user.setFriends(new HashSet<>());
    }

    @Test
    public void addFilm_add_correct_film_in_bd() {
        filmDbStorage.addFilm(film);
        Assertions.assertTrue(filmDbStorage.checkFilm(film.getId()));
    }

    @Test
    public void updateFilm_correct_update_from_db() {
        filmDbStorage.addFilm(film);
        film.setName("newName");
        film.setId(1);
        filmDbStorage.updateFilm(film);
        film.setName("name");

        Assertions.assertNotEquals(film, filmDbStorage.getFilmById(film.getId()));
    }

    @Test
    public void likeAndDeleteLike_add_like_and_delete_like_correct_result() {
        filmDbStorage.addFilm(film);
        userDbStorage.addUser(user);
        filmDbStorage.addLike(1, 1);
        Assertions.assertFalse(filmDbStorage.getLikesByFilmId(1).isEmpty());
    }

}
