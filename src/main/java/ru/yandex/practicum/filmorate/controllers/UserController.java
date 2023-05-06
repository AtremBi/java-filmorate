package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    @GetMapping("/users/{id}")
    public User getUsers(@PathVariable int id) {
        if (userStorage.getUsers().containsKey(id)) {
            return userStorage.getUsers().get(id);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        if (userStorage.getUsers().containsKey(id)) {
            List<User> users = new ArrayList<>();
            for (Integer friendId : userStorage.getUsers().get(id).getFriends()) {
                users.add(userStorage.getUsers().get(friendId));
            }
            return users;
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(otherId)) {
            return userService.getCommonFriends(id, otherId);
        } else if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriends(@PathVariable int id, @PathVariable int friendId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            return userService.addFriend(id, friendId);
        } else if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    private User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            return userService.deleteFriend(id, friendId);
        } else if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }

    }
}
