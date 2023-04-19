package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.exceptions.ValidException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    //При ошибках не возвращаю коды, но вроде как это должны дальше разбирать.
    //Поэтому не стал заморачиваться и гуглить :)
    private int id;
    Map<Integer, User> mapUsers = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers(){
        return new ArrayList<>(mapUsers.values());
    }

    @PostMapping("/users")
    public User postUser(@Valid @RequestBody User user){
        try {
            if (!mapUsers.containsKey(user.getId())) {
                id++;
                user.setId(id);
                return validation(user);
            } else {
                throw new ValidException();
            }
        } catch (ValidException e) {
            e.getMessage();
        }
        return null;
    }

    @PutMapping("/users")
    public User putUser(@Valid @RequestBody User user){
        try {
            if (mapUsers.containsKey(user.getId())){
                return validation(user);
            } else {
                throw new ValidException();
            }
        } catch (ValidException e){
            e.getMessage();
        }
        return null;
    }

    private User validation(User user) throws NullPointerException{
        try {
            if (user.getBirthday().isBefore(LocalDate.now())
            && user.getEmail().contains("@") &&
            !user.getLogin().isBlank()) {
                if (user.getName() == null){
                    user.setName(user.getLogin());
                }
                log.info("Пользователь обновлен/создан {}", user);
                mapUsers.put(user.getId(), user);
                return mapUsers.get(user.getId());
            } else {
                throw new ValidException();
            }
        } catch (ValidException e) {
            e.getMessage();
        }
        return null;
    }
}
