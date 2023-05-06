package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id;
    private final Map<Integer, User> mapUsers = new HashMap<>();

    private boolean validation(User user) throws NullPointerException {
        if (user.getBirthday().isBefore(LocalDate.now()) && user.getEmail().contains("@")
                && !user.getLogin().isBlank()) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            log.info("Пользователь обновлен/создан {}", user);
            return true;
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public User addUser(User user) {
        if (validation(user)) {
            if (!mapUsers.containsKey(user.getId())) {
                id++;
                user.setId(id);
                mapUsers.put(user.getId(), user);
                return mapUsers.get(user.getId());
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public User updateUser(User user) {
        if (validation(user)) {
            if (mapUsers.containsKey(user.getId())) {
                mapUsers.put(user.getId(), user);
                return mapUsers.get(user.getId());
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Map<Integer, User> getUsers() {
        return mapUsers;
    }
}
