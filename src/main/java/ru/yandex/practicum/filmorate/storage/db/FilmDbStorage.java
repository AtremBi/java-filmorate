package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("films").usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        log.info("В базу добавлен фильм. id - {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        log.info("В базе обновлен фильм с id {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM films;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }

    @Override
    public boolean checkFilm(int id) {
        String sqlQuery = "SELECT *" +
                "FROM films WHERE id = ?;";
        return !jdbcTemplate.query(sqlQuery, this::mapRowToFilms, id).isEmpty();
    }

    @Override
    public Film getFilmById(int filmId) {
        String sqlQuery = "SELECT *" +
                "FROM films WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilms, filmId);
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sqlQuery = "INSERT into likes (film_id, user_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sql = "DELETE FROM likes WHERE user_id = ? and film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public Set<Integer> getLikesByFilmId(int filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapLikes, filmId));
    }

    private Integer mapLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(Mpa.builder().id(resultSet.getInt("mpa_id")).build())
                .build();
    }
}
