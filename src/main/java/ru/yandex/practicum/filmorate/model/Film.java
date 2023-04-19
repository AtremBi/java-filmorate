package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    int id;
    @NotBlank
    @NonNull
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    Integer duration;
}
