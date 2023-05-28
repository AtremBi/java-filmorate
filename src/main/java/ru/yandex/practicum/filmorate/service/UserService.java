package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (User user : storage.getUsers()) {
            users.add(buildUser(user));
        }
        return users;
    }

    public User addUser(User user) {
        if (validation(user)) {
            return buildUser(storage.addUser(user));
        } else {
            throw new ValidationException();
        }
    }

    public User updateUser(User user) {
        if (validation(user)) {
            if (storage.checkUser(user.getId())) {
                return buildUser(storage.updateUser(user));
            } else {
                throw new NotFoundException("Пользователь не найден");
            }
        } else {
            throw new ValidationException();
        }
    }


    public User getUserById(int id) {
        if (storage.checkUser(id)) {
            return buildUser(storage.getUserById(id));
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public User buildUser(User user) {
        for (Integer friendId : storage.getFriends(user)) {
            user.getFriends().add(friendId);
        }
        return user;
    }

    public List<User> getFriends(int id) {
        if (storage.checkUser(id)) {
            List<User> friends = new ArrayList<>();
            for (Integer friendId : storage.getFriends(getUserById(id))) {
                friends.add(buildUser(storage.getUserById(friendId)));
            }
            return friends;
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (checkUserAndFriend(userId, friendId)) {
            storage.addFriend(userId, friendId);
            log.info("Добавлен друг в персону 1 - {}", storage.getUserById(userId));
            return buildUser(storage.getUserById(userId));
        } else if (!storage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public User deleteFriend(int userId, int friendId) {
        if (checkUserAndFriend(userId, friendId)) {
            storage.deleteFriend(userId, friendId);
            log.info("Удален друг в персоне 1 - {}", storage.getUserById(userId));
            return buildUser(storage.getUserById(userId));
        } else if (!storage.checkUser(userId)) {
            throw new NotFoundException("Пользователь не найден");
        } else {
            throw new NotFoundException("Друг не найден");
        }
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        if (checkUserAndFriend(firstUserId, secondUserId)) {
            Set<Integer> friendsFirstUser = new HashSet<>(storage.getFriends(storage.getUserById(firstUserId)));
            friendsFirstUser.retainAll(storage.getFriends(storage.getUserById(secondUserId)));
            List<User> mutualFriends = new ArrayList<>();
            for (int userId : friendsFirstUser) {
                mutualFriends.add(buildUser(storage.getUserById(userId)));
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
