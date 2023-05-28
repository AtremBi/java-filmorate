package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Integer id;
    @NotBlank
    @NonNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private final Set<Integer> likes = new HashSet<>();
    private final Set<Genre> genres = new HashSet<>();
    private Mpa mpa;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        return values;
    }


}
