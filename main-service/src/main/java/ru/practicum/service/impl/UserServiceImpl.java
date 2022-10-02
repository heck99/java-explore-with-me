package ru.practicum.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserServiceFull;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceFull {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto createUser(UserDto user) {
        return userMapper.toUserDto(userRepository.save(userMapper.fromUserDto(user)));
    }

    @Override
    public List<UserDto> getUsers(int from, int size, List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids).stream().map(userMapper::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from / size, size)).stream().map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(int userId) {
        getUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
    }

    public UserDto getUserByIdOrThrow(int id) {
        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("пользователь с idf = %d не найден", id))));
    }

}
