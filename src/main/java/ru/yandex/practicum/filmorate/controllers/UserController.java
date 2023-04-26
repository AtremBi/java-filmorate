package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private int id;
    private Map<Integer, User> mapUsers = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
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

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {
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

    private boolean validation(User user) throws NullPointerException {
        if (user.getBirthday().isBefore(LocalDate.now())
                && user.getEmail().contains("@") &&
                !user.getLogin().isBlank()) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.info("Пользователь обновлен/создан {}", user);
            return true;
        } else {
            throw new ValidationException();
        }
    }
}
