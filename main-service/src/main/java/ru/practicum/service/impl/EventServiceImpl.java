package ru.practicum.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.SortType;
import ru.practicum.dto.*;
import ru.practicum.exception.IncorrectParameters;
import ru.practicum.exception.NoAccess;
import ru.practicum.exception.NotFound;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;

import ru.practicum.mapper.UserMapper;
import ru.practicum.model.*;
import ru.practicum.repository.CustomEventRepository;
import ru.practicum.repository.EventRepository;

import ru.practicum.service.CategoryService;
import ru.practicum.service.EventServiceFull;
import ru.practicum.service.RequestServiceFull;
import ru.practicum.service.UserServiceFull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventServiceFull {

    private final EventRepository eventRepository;
    private final EventMapper em = new EventMapper();
    private final UserMapper um = new UserMapper();
    private final CategoryMapper cm = new CategoryMapper();
    private final UserServiceFull userService;
    private final CategoryService categoryService;

    private final RequestServiceFull requestService;

    private final CustomEventRepository customEventRepository;

    public EventServiceImpl(EventRepository eventRepository, UserServiceFull userService, CategoryService categoryService,
                            @Lazy RequestServiceFull requestService, CustomEventRepository customEventRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.customEventRepository = customEventRepository;
        this.requestService = requestService;
    }


    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, int userId) {
        Event newEvent = em.fromNewEventDto(newEventDto);
        newEvent.setInitiator(um.fromUserDto(userService.getUserById(userId)));
        newEvent.setCreated(LocalDateTime.now());
        LocalDateTime eventDate = newEvent.getEventDate();
        newEvent.setCategory(cm.fromCategoryDto(categoryService.getCategoryById(newEvent.getCategory().getId())));
        newEvent.setViews(0);
        if (eventDate.minusHours(2).isBefore(LocalDateTime.now())) {
            throw new IncorrectParameters("дата и время события не может быть раньше, чем через два часа от текущего момента");
        }
        newEvent.setState(State.PENDING);
        return em.toEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto updateEvent(UpdateEventRequest eventDto, int userId) {
        Event event = eventRepository.findById(eventDto.getEventId()).orElseThrow(NotFound::new);

        if (event.getInitiator().getId() != userId) {
            throw new NoAccess();
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(cm.fromCategoryDto(categoryService.getCategoryById(eventDto.getCategory())));
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        return em.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEvent(AdminUpdateEventRequest eventDto, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            event.setCategory(cm.fromCategoryDto(categoryService.getCategoryById(eventDto.getCategory())));
        }

        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }

        if (eventDto.getLocation() != null) {
            event.setLatitude(eventDto.getLocation().getLat());
            event.setLongitude(eventDto.getLocation().getLon());
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        return em.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllUsersEvents(int userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream().map(em::toEventShortDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUser(int eventId, int userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        if (event.getInitiator().getId() != userId) {
            throw new NoAccess();
        }

        EventFullDto toReturn = em.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public EventFullDto cancelEvent(int eventId, int userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        if (event.getState() != State.PENDING) {
            throw new NoAccess();
        }
        event.setState(State.CANCELED);
        return em.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publicEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new NoAccess();
        }

        if (event.getState() != State.PENDING) {
            throw new NoAccess();
        }
        event.setState(State.PUBLISHED);
        return em.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        if (event.getState() != State.PENDING) {
            throw new NoAccess();
        }
        event.setState(State.CANCELED);
        return em.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getPublishedEvent(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        if (event.getState() != State.PUBLISHED) {
            throw new NoAccess();
        }
        EventFullDto toReturn = em.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public EventFullDto getEventById(int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFound::new);
        eventRepository.addView(eventId);
        EventFullDto toReturn = em.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public List<EventFullDto> getAllAdmin(List<Integer> users, List<State> states, List<Integer> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<User> userList = null;
        if (users != null) {
            userList = users.stream().map(element -> um.fromUserDto(userService.getUserById(element))).collect(Collectors.toList());
        }
        List<Category> categoryList = null;
        if (categories != null) {
            categoryList = categories.stream().map(element -> cm.fromCategoryDto(categoryService.getCategoryById(element))).collect(Collectors.toList());
        }
        List<Event> toReturn = customEventRepository.getAllAdmin(userList, states, categoryList, rangeStart, rangeEnd, from, size);
        return toReturn.stream().map(em::toEventFullDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getAllUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size) {

        List<Event> toReturn = customEventRepository.getAllUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return toReturn.stream().map(em::toEventFullDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());

    }

}
