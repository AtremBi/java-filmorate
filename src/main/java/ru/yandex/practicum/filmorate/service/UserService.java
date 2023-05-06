package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    UserStorage storage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.storage = inMemoryUserStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        User friend = storage.getUsers().get(friendId);
        storage.getUsers().get(userId).getFriends().add(friend.getId());
        log.info("Добавлен друг в персону 1 - {}", storage.getUsers().get(userId));
        storage.getUsers().get(friendId).getFriends().add(userId);
        log.info("Добавлен друг в персону 2 - {}", storage.getUsers().get(friend.getId()));
        return storage.getUsers().get(userId);
    }

    public User deleteFriend(int userId, int friendId) {
        storage.getUsers().get(userId).getFriends().remove(friendId);
        log.info("Удален друг в персоне 1 - {}", storage.getUsers().get(userId));
        storage.getUsers().get(friendId).getFriends().remove(userId);
        log.info("Удален друг в персоне 2 - {}", storage.getUsers().get(friendId));
        return storage.getUsers().get(userId);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        Set<Integer> friendsFirstUser = new HashSet<>(storage.getUsers().get(firstUserId).getFriends());
        friendsFirstUser.retainAll(storage.getUsers().get(secondUserId).getFriends());
        List<User> mutualFriends = new ArrayList<>();
        for (int userId : friendsFirstUser) {
            mutualFriends.add(storage.getUsers().get(userId));
        }
        log.info("Выведен список общих друзей пользователей с id - {}, {}", firstUserId, secondUserId);
        return mutualFriends;
    }
}
