package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.dto.*;
import ru.practicum.exception.IncorrectParameters;
import ru.practicum.exception.NotFound;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.service.UserServiceFull;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        eventService = new EventServiceImpl(repository, userService, categoryService, null, null);
    }

    @Test
    public void creatEventCorrect() {
        when(categoryService.getCategoryById(1)).thenReturn(category1Dto);
        when(userService.getUserById(1)).thenReturn(user1Dto);
        when(repository.save(any())).thenReturn(event1);
        EventFullDto event = eventService.createEvent(newEventDto, 1);
        assertEquals(event.getId(), 1);
        assertEquals(event.getEventDate(), newEventTime);
        assertEquals(event.getInitiator().getId(), 1);
        assertTrue(event.getCreatedOn().isBefore(LocalDateTime.now()));
        verify(categoryService, times(1)).getCategoryById(1);
        verify(userService, times(1)).getUserById(1);
        verify(repository, times(1)).save(any());
    }

    @Test
    public void creatEventUserNotFound() {
        when(userService.getUserById(1)).thenThrow(NotFound.class);
        assertThrows(NotFound.class, () -> eventService.createEvent(newEventDto, 1));
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void creatEventCategoryNotFound() {
        when(categoryService.getCategoryById(1)).thenThrow(NotFound.class);
        when(userService.getUserById(1)).thenReturn(user1Dto);
        assertThrows(NotFound.class, () -> eventService.createEvent(newEventDto, 1));
        verify(categoryService, times(1)).getCategoryById(1);
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void creatEventTooEarly() {
        newEventDto.setEventDate(LocalDateTime.now());
        when(categoryService.getCategoryById(1)).thenReturn(category1Dto);
        when(userService.getUserById(1)).thenReturn(user1Dto);
        assertThrows(IncorrectParameters.class, () -> eventService.createEvent(newEventDto, 1));
        verify(categoryService, times(1)).getCategoryById(1);
        verify(userService, times(1)).getUserById(1);
        newEventDto.setEventDate(newEventTime);
    }
}