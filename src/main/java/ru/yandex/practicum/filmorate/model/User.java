package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private Integer id;
    @Email
    @NotBlank
    @NonNull
    private String email;
    @NotBlank
    @NonNull
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends;
}
