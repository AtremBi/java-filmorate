package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id;
    private final Map<Integer, User> mapUsers = new HashMap<>();

    @Override
    public User addUser(User user) {
        id++;
        user.setId(id);
        mapUsers.put(user.getId(), user);
        return mapUsers.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        mapUsers.put(user.getId(), user);
        return mapUsers.get(user.getId());
    }

    public User getUserById(int id) {
        return mapUsers.get(id);
    }

    @Override
    public boolean checkUser(int id) {
        return mapUsers.containsKey(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(mapUsers.values());
    }
}
