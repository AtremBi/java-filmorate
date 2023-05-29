package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("films").usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(toMapFilms(film)).intValue());
        genreStorage.createFilmGenre(film);

        film.setGenres(genreStorage.getGenreByFilm(film));
        film.setLikes(getLikesByFilmId(film.getId()));
        film.setMpa(getFilmById(film.getId()).getMpa());
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
        genreStorage.createFilmGenre(film);
        film.setGenres(genreStorage.getGenreByFilm(film));
        film.setLikes(getLikesByFilmId(film.getId()));
        film.setMpa(getFilmById(film.getId()).getMpa());
        log.info("В базе обновлен фильм с id {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery =
                "SELECT f.ID , \n" +
                        "f.NAME, \n" +
                        "f.DESCRIPTION,\n" +
                        "f.RELEASE_DATE, \n" +
                        "f.DURATION,\n" +
                        "m.ID as MPA_ID ,\n" +
                        "m.NAME as MPA_NAME\n" +
                        "FROM FILMS f \n" +
                        "JOIN MPA m ON f.MPA_ID  = m.ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }

    @Override
    public boolean checkFilm(int id) {
        String sqlQuery =
                "SELECT id\n" +
                        "FROM FILMS \n" +
                        "WHERE ID = ?;";
        return !jdbcTemplate.query(sqlQuery, this::mapFilmId, id).isEmpty();
    }

    @Override
    public Film getFilmById(int filmId) {
        String sqlQuery =
                "SELECT f.ID , \n" +
                        "f.NAME, \n" +
                        "f.DESCRIPTION,\n" +
                        "f.RELEASE_DATE, \n" +
                        "f.DURATION,\n" +
                        "m.ID as MPA_ID ,\n" +
                        "m.NAME as MPA_NAME\n" +
                        "FROM FILMS f \n" +
                        "JOIN MPA m ON f.MPA_ID  = m.ID\n" +
                        "WHERE f.ID = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms, filmId).get(0);
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

    private Integer mapFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("id");
    }

    private Integer mapLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .build();
        film.setGenres(genreStorage.getGenreByFilm(film));
        film.setLikes(getLikesByFilmId(film.getId()));
        return film;
    }


    private Map<String, Object> toMapFilms(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }
}
