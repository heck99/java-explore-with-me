package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.Location;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.IncorrectParametersException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.service.UserServiceFull;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    EventServiceImpl eventService;

    @Mock
    EventRepository repository;

    @Mock
    UserServiceFull userService;

    @Mock
    CategoryService categoryService;

    CategoryMapper categoryMapper;

    EventMapper eventMapper;

    UserMapper userMapper;

    LocalDateTime newEventTime = LocalDateTime.now().plusHours(3);
    NewEventDto newEventDto = new NewEventDto("annotation1", 1, "description1",
            newEventTime, new Location(100.2, 200.1), true, 0,
            true, "title1");
    Category category1 = new Category(1, "category1");
    UserDto user1Dto = new UserDto(1, "user1", "user1@mail.ru");
    User user1 = new User(1, "user1", "user1@mail.ru");

    Event event1 = new Event(1, "annotation1", category1, LocalDateTime.now(), "description1",
            newEventTime, user1, 100.2, 200.1, true, 0, null,
            false, State.PENDING, "title1", 0);
    CategoryDto category1Dto = new CategoryDto(1, "category1");

    @BeforeEach
    void init() {
        eventService = new EventServiceImpl(repository, eventMapper, userMapper, categoryMapper, userService, categoryService, null, null);
    }

    @Test
    public void creatEventCorrect() {
        when(categoryService.getCategoryByIdOrThrow(1)).thenReturn(category1Dto);
        when(userService.getUserByIdOrThrow(1)).thenReturn(user1Dto);
        when(repository.save(any())).thenReturn(event1);
        EventFullDto event = eventService.createEvent(newEventDto, 1);
        assertEquals(event.getId(), 1);
        assertEquals(event.getEventDate(), newEventTime);
        assertEquals(event.getInitiator().getId(), 1);
        assertTrue(event.getCreatedOn().isBefore(LocalDateTime.now()));
        verify(categoryService, times(1)).getCategoryByIdOrThrow(1);
        verify(userService, times(1)).getUserByIdOrThrow(1);
        verify(repository, times(1)).save(any());
    }

    @Test
    public void creatEventUserNotFound() {
        when(userService.getUserByIdOrThrow(1)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> eventService.createEvent(newEventDto, 1));
        verify(userService, times(1)).getUserByIdOrThrow(1);
    }

    @Test
    public void creatEventCategoryNotFound() {
        when(categoryService.getCategoryByIdOrThrow(1)).thenThrow(NotFoundException.class);
        when(userService.getUserByIdOrThrow(1)).thenReturn(user1Dto);
        assertThrows(NotFoundException.class, () -> eventService.createEvent(newEventDto, 1));
        verify(categoryService, times(1)).getCategoryByIdOrThrow(1);
        verify(userService, times(1)).getUserByIdOrThrow(1);
    }

    @Test
    public void creatEventTooEarly() {
        newEventDto.setEventDate(LocalDateTime.now());
        when(categoryService.getCategoryByIdOrThrow(1)).thenReturn(category1Dto);
        when(userService.getUserByIdOrThrow(1)).thenReturn(user1Dto);
        assertThrows(IncorrectParametersException.class, () -> eventService.createEvent(newEventDto, 1));
        verify(categoryService, times(1)).getCategoryByIdOrThrow(1);
        verify(userService, times(1)).getUserByIdOrThrow(1);
        newEventDto.setEventDate(newEventTime);
    }
}