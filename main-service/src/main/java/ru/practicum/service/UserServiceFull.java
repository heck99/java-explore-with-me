package ru.practicum.service;

import ru.practicum.dto.UserDto;

public interface UserServiceFull extends UserService {
    UserDto getUserByIdOrThrow(int userId);
}
