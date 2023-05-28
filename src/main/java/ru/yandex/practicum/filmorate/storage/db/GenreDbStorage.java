package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genre WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * FROM genre GROUP BY ID ORDER BY ID ASC;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public List<Integer> getGenreIdByFilm(Film film) {
        String sqlQuery = "SELECT genre_id FROM film_genre WHERE film_id = ? GROUP BY GENRE_ID ORDER BY GENRE_ID ASC ;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenreIdFromFilmGenre, film.getId());
    }

    public void createFilmGenre(Film film) {
        String sqlQuery = "INSERT into film_genre (film_id, genre_id) values(?, ?);";
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    public void deleteFilmGenre(Film film) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sql, film.getId());
    }

    private Integer mapRowToGenreIdFromFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("genre_id");
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
