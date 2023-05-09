package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private static UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        UserService.storage = storage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User addUser(User user) {
        if (validation(user)) {
            return storage.addUser(user);
        } else {
            throw new ValidationException();
        }
    }

    public User updateUser(User user) {
        if (validation(user)) {
            if (storage.checkUser(user.getId())) {
                return storage.updateUser(user);
            } else {
                throw new ValidationException();
            }
        } else {
            throw new ValidationException();
        }
    }


    public User getUserById(int id) {
        if (storage.checkUser(id)) {
            return storage.getUserById(id);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public List<User> getFriends(int id) {
        if (storage.checkUser(id)) {
            List<User> users = new ArrayList<>();
            for (Integer friendId : storage.getUserById(id).getFriends()) {
                users.add(storage.getUserById(friendId));
            }
            return users;
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (checkUserAndFriend(userId, friendId)) {
            User friend = storage.getUserById(friendId);
            storage.getUserById(userId).getFriends().add(friendId);
            log.info("Добавлен друг в персону 1 - {}", storage.getUserById(userId));
            storage.getUserById(friendId).getFriends().add(userId);
            log.info("Добавлен друг в персону 2 - {}", storage.getUserById(friend.getId()));
            return storage.getUserById(userId);
        } else if (!storage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public User deleteFriend(int userId, int friendId) {
        if (checkUserAndFriend(userId, friendId)) {
            storage.getUsers().get(userId).getFriends().remove(friendId);
            log.info("Удален друг в персоне 1 - {}", storage.getUserById(userId));
            storage.getUsers().get(friendId).getFriends().remove(userId);
            log.info("Удален друг в персоне 2 - {}", storage.getUserById(friendId));
            return storage.getUserById(userId);
        } else if (!storage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        if (checkUserAndFriend(firstUserId, secondUserId)) {
            Set<Integer> friendsFirstUser = new HashSet<>(storage.getUserById(firstUserId).getFriends());
            friendsFirstUser.retainAll(storage.getUserById(secondUserId).getFriends());
            List<User> mutualFriends = new ArrayList<>();
            for (int userId : friendsFirstUser) {
                mutualFriends.add(storage.getUserById(userId));
            }
            log.info("Выведен список общих друзей пользователей с id - {}, {}", firstUserId, secondUserId);
            return mutualFriends;
        } else if (!storage.checkUser(firstUserId)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    private boolean checkUserAndFriend(int id, int otherId) {
        return storage.checkUser(id) && storage.checkUser(otherId);
    }

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
}
