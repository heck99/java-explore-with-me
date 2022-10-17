package ru.practicum.service;

import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    List<UserDto> getUsers(int from, int size, List<Integer> ids);

    void deleteUser(int userId);
}
