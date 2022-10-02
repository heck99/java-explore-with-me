package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@AllArgsConstructor
public class UserAdminController {

    UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "from", defaultValue = "0") int from,
                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                  @RequestParam List<Integer> ids) {
        return userService.getUsers(from, size, ids);
    }

}
