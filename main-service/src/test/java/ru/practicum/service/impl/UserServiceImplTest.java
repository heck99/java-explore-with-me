package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    UserServiceImpl service;

    @Mock
    UserRepository userRepository;

    UserMapper userMapper;

    User user1 = new User(1, "user1", "user1@mail.com");
    User user2 = new User(2, "user2", "user2@mail.com");
    User user3 = new User(3, "user3", "user3@mail.com");
    UserDto newUserDto = new UserDto(null, "user1", "user1@mail.com");

    @BeforeEach
    void init() {
        service = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    public void testCreateUserCorrect() {
        when(userRepository.save(any())).thenReturn(user1);
        UserDto userDto = service.createUser(newUserDto);
        Assertions.assertEquals(userDto.getId(), 1);
        Assertions.assertEquals(userDto.getName(), "user1");
        Assertions.assertEquals(userDto.getEmail(), "user1@mail.com");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteUserCorrect() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        service.deleteUser(1);
        verify(userRepository, times(1)).deleteById(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.deleteUser(1));
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void testGetUsersByIdsCorrect() {
        when(userRepository.findByIdIn(any())).thenReturn(List.of(user1, user2, user3));
        List<UserDto> list = service.getUsers(10, 10, List.of(1, 2, 3));
        Assertions.assertEquals(list.size(), 3);
        Assertions.assertEquals(list.get(0).getId(), 1);
        Assertions.assertEquals(list.get(1).getId(), 2);
        Assertions.assertEquals(list.get(2).getId(), 3);
        verify(userRepository, times(1)).findByIdIn(any());
    }

    @Test
    public void testGetAllUsersCorrect() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(user1, user2, user3)));
        List<UserDto> list = service.getUsers(10, 10, null);
        Assertions.assertEquals(list.size(), 3);
        Assertions.assertEquals(list.get(0).getId(), 1);
        Assertions.assertEquals(list.get(1).getId(), 2);
        Assertions.assertEquals(list.get(2).getId(), 3);
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

}