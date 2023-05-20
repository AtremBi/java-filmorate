package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    boolean checkUser(int id);

    User getUserById(int id);
}
