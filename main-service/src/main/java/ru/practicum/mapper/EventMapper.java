package ru.practicum.mapper;

import ru.practicum.dto.*;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;

public class EventMapper {
    public Event fromNewEventDto(NewEventDto dto) {
        return new Event(null, dto.getAnnotation(), new Category(dto.getCategory(), ""), null, dto.getDescription(),
                dto.getEventDate(), null, dto.getLocation().getLat(), dto.getLocation().getLon(), dto.getPaid(),
                dto.getParticipantLimit(), null, dto.getRequestModeration(), null, dto.getTitle(), null);
    }

    public Event fromAdminUpdateEventRequest(AdminUpdateEventRequest dto) {

        return new Event(null, dto.getAnnotation(), new Category(dto.getCategory(), null), null, dto.getDescription(),
                dto.getEventDate(), null, dto.getLocation().getLat(), dto.getLocation().getLon(), dto.getPaid(),
                dto.getParticipantLimit(), null, dto.getRequestModeration(), null, dto.getTitle(), null);
    }

    public Event fromEventFullDto(EventFullDto dto) {
        return new Event(dto.getId(), dto.getAnnotation(), new Category(dto.getCategory().getId(), dto.getCategory().getName()),
                dto.getCreatedOn(), dto.getDescription(), dto.getEventDate(), new User(dto.getInitiator().getId(), dto.getInitiator().getName(), null),
                dto.getLocation().getLat(), dto.getLocation().getLon(), dto.getPaid(), dto.getParticipantLimit(),
                dto.getPublishedOn(), dto.getRequestModeration(), dto.getState(), dto.getTitle(), dto.getViews());
    }

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(), event.getAnnotation(),
                new CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                null, event.getCreated(), event.getDescription(), event.getEventDate(),
                new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                new Location(event.getLatitude(), event.getLongitude()), event.getPaid(), event.getParticipantLimit(),
                event.getPublished(), event.getRequestModeration(), event.getState(), event.getTitle(), event.getViews(), null, null);
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(), event.getAnnotation(),
                new CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                null, event.getEventDate(), new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getPaid(), event.getTitle(), event.getViews(), null, null);
    }
}
