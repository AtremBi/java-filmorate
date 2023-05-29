package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaControllerTest {
    private final MpaController controller;

    @Test
    public void getAllMpa_get_not_empty_list_mpa_from_db() {
        Assertions.assertFalse(controller.getAllMpa().isEmpty());
    }

    @Test
    public void getMpaById_get_mpa_by_id_from_db() {
        Assertions.assertEquals(Mpa.builder().id(2).name("PG").build(), controller.getMpaById(2));
    }

    @Test
    public void getNotFoundException_get_exception_by_search_mpa_with_unknown_id() {
        Assertions.assertThrows(NotFoundException.class, () -> controller.getMpaById(9));
    }
}
