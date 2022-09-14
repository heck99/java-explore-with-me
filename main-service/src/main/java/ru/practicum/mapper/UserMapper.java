package ru.practicum.mapper;

import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.User;

public class UserMapper {
    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User fromUserDto(UserDto dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail());
    }

    public User fromUserShortDto(UserShortDto dto) {
        return new User(dto.getId(), dto.getName(), null);
    }
}
