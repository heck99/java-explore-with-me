package ru.practicum.service;

import ru.practicum.dto.EventFullDto;

public interface EventServiceFull extends EventService {
    EventFullDto getEventByIdOrThrow(int eventId);
}
