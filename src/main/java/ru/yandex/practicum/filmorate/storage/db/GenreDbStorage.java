package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) {
        String sql =
                "SELECT g.ID ," +
                        "g.NAME " +
                        "FROM GENRE g " +
                        "WHERE g.ID = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * FROM genre GROUP BY ID ORDER BY ID ASC;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Map<Integer, Integer> getAllFilmGenre() {
        String sqlQuery = "SELECT * FROM film_genre;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenreIdFromFilmGenre);
    }

    @Override
    public Set<Genre> getGenreByFilm(Film film) {
        String sqlQuery =
                "SELECT g.ID ,\n" +
                        "g.NAME \n" +
                        "FROM GENRE g\n" +
                        "JOIN film_genre fg ON fg.GENRE_ID = g.ID \n" +
                        "WHERE fg.film_id = ? \n " +
                        "GROUP BY ID ORDER BY ID ASC;";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, film.getId()));
    }

    @Override
    public void createFilmGenre(Film film) {
        String sqlQuery = "INSERT into film_genre (film_id, genre_id) values(?, ?);";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void deleteFilmGenre(Film film) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sql, film.getId());
    }

    private Map<Integer, Integer> mapRowToGenreIdFromFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(resultSet.getInt("genre_id"), resultSet.getInt("film_id"));
        return map;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
