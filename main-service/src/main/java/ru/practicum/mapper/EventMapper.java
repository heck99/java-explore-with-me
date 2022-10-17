package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.Location;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;

@Component
public class EventMapper {
    public Event fromNewEventDto(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .category(new Category(dto.getCategory(), ""))
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .latitude(dto.getLocation().getLat())
                .longitude(dto.getLocation().getLon())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .views(0)
                .build();

    }

    public Event fromEventFullDto(EventFullDto dto) {
        return Event.builder()
                .id(dto.getId())
                .annotation(dto.getAnnotation())
                .category(new Category(dto.getCategory().getId(), dto.getCategory().getName()))
                .created(dto.getCreatedOn())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .initiator(new User(dto.getInitiator().getId(), dto.getInitiator().getName(), null))
                .latitude(dto.getLocation().getLat())
                .longitude(dto.getLocation().getLon())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .published(dto.getPublishedOn())
                .requestModeration(dto.getRequestModeration())
                .state(dto.getState())
                .title(dto.getTitle())
                .views(dto.getViews())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .createdOn(event.getCreated())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(new Location(event.getLatitude(), event.getLongitude()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublished())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
