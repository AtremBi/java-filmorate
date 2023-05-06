package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Component
public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Map<Integer, User> getUsers();
}
