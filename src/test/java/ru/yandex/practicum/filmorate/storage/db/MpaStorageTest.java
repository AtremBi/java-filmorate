package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getAllMpa_callMethodGetAllMpa() {
        Assertions.assertEquals(5, mpaDbStorage.getAllMpa().size());
    }

    @Test
    public void getMpaById_mpaExistInBd() {
        Assertions.assertEquals(Mpa.builder().id(2).name("PG").build(), mpaDbStorage.getMpaById(2));
    }

    @Test
    public void getNotFoundException_mpaNotExistInBd() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> mpaDbStorage.getMpaById(9));
    }
}
