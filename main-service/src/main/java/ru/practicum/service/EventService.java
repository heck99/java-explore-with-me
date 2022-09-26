package ru.practicum.service;

import ru.practicum.dto.SortType;
import ru.practicum.dto.*;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEvent, int userId);

    EventFullDto updateEvent(UpdateEventRequest eventDto, int userId);

    EventFullDto updateEvent(AdminUpdateEventRequest eventDto, int eventId);

    List<EventShortDto> getAllUsersEvents(int userId, int from, int size);

    EventFullDto getEventByUser(int eventId, int userId);

    EventFullDto cancelEvent(int eventId, int userId);

    EventFullDto rejectEvent(int eventId);

    EventFullDto publicEvent(int eventId);

    EventFullDto getPublishedEvent(int eventId);

    List<EventFullDto> getAllAdmin(List<Integer> users, List<State> states, List<Integer> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventFullDto> getAllUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size);
}
