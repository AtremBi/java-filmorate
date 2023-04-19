package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.Assert.*;

public class UserControllerTest {
    private User user;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        user = User.builder()
                .email("asd@sad.ru")
                .login("asdada")
                .name("qwerty")
                .birthday(LocalDate.of(2007, 9, 1))
                .build();
    }

    @Test
    public void email_without_dog_not_valid() {
        user.setEmail("12323");
        assertThrows(ValidationException.class, () -> userController.postUser(user));
    }

    @Test
    public void email_with_dog_is_valid() {
        userController.postUser(user);
        assertFalse(userController.getUsers().isEmpty());
    }

    @Test
    public void email_is_empty_not_valid() {
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    public void login_with_whitespace_not_valid() {
        user.setLogin("asd asd");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void empty_name_is_replaced_with_a_login() {
        user.setName(null);
        userController.postUser(user);
        assertEquals(user.getLogin(), userController.getUsers().get(0).getName());
    }

    @Test
    public void date_of_birth_cannot_be_in_the_future() {
        user.setBirthday(LocalDate.of(2895, DECEMBER, 28));
        assertThrows(ValidationException.class, () -> userController.postUser(user));
    }

}
