package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private User user;
    private final UserController userController;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("asd@sad.ru")
                .login("asdada")
                .name("qwerty")
                .birthday(LocalDate.of(2007, 9, 1))
                .build();
    }

    @Test
    public void getValidationException_emailWithoutDog() {
        user.setEmail("12323");
        assertThrows(ValidationException.class, () -> userController.postUser(user));
    }

    @Test
    public void userCreate_emailWithDog() {
        userController.postUser(user);
        assertFalse(userController.getUsers().isEmpty());
    }

    @Test
    public void userNotCreate_emptyEmail() {
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    public void userNotCreate_loginWithWhitespace() {
        user.setLogin("asd asd");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void nameIsReplacedOnLogin_emptyName() {
        user.setName("");
        userController.postUser(user);
        assertEquals(user.getLogin(), userController.getUsers().get(0).getName());
    }

    @Test
    public void userNotCreate_dateOfBirthInTheFuture() {
        user.setBirthday(LocalDate.of(2895, DECEMBER, 28));
        assertThrows(ValidationException.class, () -> userController.postUser(user));
    }

}
