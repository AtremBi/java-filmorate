package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        log.info("В базу добавлен новый пользователь. id - {}", user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Пользователь обновлен. id - {}", user.getId());
        return getUserById(user.getId());
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUsers);
    }

    @Override
    public boolean checkUser(int id) {
        String sqlQuery = "select id, email, login, name, birthday " +
                "FROM users WHERE id = ?;";
        return !jdbcTemplate.query(sqlQuery, this::mapRowToUsers, id).isEmpty();
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "select id, email, login, name, birthday " +
                "FROM users WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUsers, id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT into friends (user_Id, friend_Id) values(?, ?);";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<Integer> getFriends(User user) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriedIdFromFriends, user.getId());
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sql, userId, friendId);
    }

    private Integer mapRowToFriedIdFromFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }

    private User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
