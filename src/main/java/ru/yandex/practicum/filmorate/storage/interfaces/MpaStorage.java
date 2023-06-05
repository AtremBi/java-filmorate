package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpaById(int id);

    List<Mpa> getAllMpa();
}
