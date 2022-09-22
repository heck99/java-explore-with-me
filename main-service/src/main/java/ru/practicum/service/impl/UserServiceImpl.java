package ru.practicum.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NotFound;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserServiceFull;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceFull {

    private final UserRepository userRepository;
    private final UserMapper um = new UserMapper();


    @Override
    public UserDto createUser(UserDto user) {
        return um.toUserDto(userRepository.save(um.fromUserDto(user)));
    }

    @Override
    public List<UserDto> getUsers(int from, int size, List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids).stream().map(um::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from / size, size)).stream().map(um::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(int userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    public UserDto getUserById(int id) {
        return um.toUserDto(userRepository.findById(id).orElseThrow(NotFound::new));
    }


}
