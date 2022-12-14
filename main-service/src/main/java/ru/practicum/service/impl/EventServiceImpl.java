package ru.practicum.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.AdminUpdateEventRequest;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.SortType;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.exception.IncorrectParametersException;
import ru.practicum.exception.NoAccessException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;

import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;
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
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final UserServiceFull userService;
    private final CategoryService categoryService;

    private final RequestServiceFull requestService;

    private final CustomEventRepository customEventRepository;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, UserMapper userMapper, CategoryMapper categoryMapper, UserServiceFull userService, CategoryService categoryService,
                            @Lazy RequestServiceFull requestService, CustomEventRepository customEventRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.userService = userService;
        this.categoryService = categoryService;
        this.customEventRepository = customEventRepository;
        this.requestService = requestService;
    }


    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, int userId) {
        Event newEvent = eventMapper.fromNewEventDto(newEventDto);
        newEvent.setInitiator(userMapper.fromUserDto(userService.getUserByIdOrThrow(userId)));
        newEvent.setCreated(LocalDateTime.now());
        LocalDateTime eventDate = newEvent.getEventDate();
        newEvent.setCategory(categoryMapper.fromCategoryDto(categoryService.getCategoryByIdOrThrow(newEvent.getCategory().getId())));
        newEvent.setViews(0);
        int minimumHoursToCreateEvent = 2;
        if (eventDate.minusHours(minimumHoursToCreateEvent).isBefore(LocalDateTime.now())) {
            throw new IncorrectParametersException("???????? ?? ?????????? ?????????????? ???? ?????????? ???????? ????????????, ?????? ?????????? ?????? ???????? ???? ???????????????? ??????????????");
        }
        newEvent.setState(State.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto updateEvent(UpdateEventRequest eventDto, int userId) {
        Event event = eventRepository.findById(eventDto.getEventId())
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventDto.getEventId())));

        if (event.getInitiator().getId() != userId) {
            throw new NoAccessException(String.format("???????????????????????? ?? id = %d ???? ?????????? ?????????????????????????? ?????????????? ?? id = %d", userId, eventDto.getEventId()));
        }

        if (event.getState() == State.PUBLISHED) {
            throw new NoAccessException("???????????? ???????????????? ???????????????????????????? ??????????????");
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryMapper.fromCategoryDto(categoryService.getCategoryByIdOrThrow(eventDto.getCategory())));
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
        event.setState(State.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEvent(AdminUpdateEventRequest eventDto, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            event.setCategory(categoryMapper.fromCategoryDto(categoryService.getCategoryByIdOrThrow(eventDto.getCategory())));
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

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAllUsersEvents(int userId, int from, int size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).stream()
                .map(eventMapper::toEventShortDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUserOrThrow(int eventId, int userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        if (event.getInitiator().getId() != userId) {
            throw new NoAccessException(String.format("???????????????????????? ?? id = %d ???? ?????????? ???????????? ?? ?????????????? ?? id = %d", userId, eventId));
        }
        EventFullDto toReturn = eventMapper.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public EventFullDto cancelEvent(int eventId, int userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        if (event.getState() != State.PENDING) {
            throw new NoAccessException("???????????? ???????????????? ??????????????, ?????????????? ???? ?????????????????? ?? ?????????????????? ???????????????? ??????????????????????????");
        }
        event.setState(State.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publicEvent(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        int minimumHoursToCPublishEvent = 1;
        if (event.getEventDate().minusHours(minimumHoursToCPublishEvent).isBefore(LocalDateTime.now())) {
            throw new NoAccessException("???????????? ?????????????????????? ?????????????? , ???? ???????????? ???????????????? ???????????????? ?????????? ????????");
        }

        if (event.getState() != State.PENDING) {
            throw new NoAccessException("???????????? ?????????????????????? ??????????????, ?????????????? ???? ?????????????????? ?? ?????????????????? ???????????????? ??????????????????????????");
        }
        event.setState(State.PUBLISHED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        if (event.getState() != State.PENDING) {
            throw new NoAccessException("???????????? ?????????????????? ??????????????, ?????????????? ???? ?????????????????? ?? ?????????????????? ???????????????? ??????????????????????????");
        }
        event.setState(State.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getPublishedEventOrThrow(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????????????????", eventId));
        }
        EventFullDto toReturn = eventMapper.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public EventFullDto getEventByIdOrThrow(int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("?????????????? ?? id = %d ???? ????????????", eventId)));
        eventRepository.addView(eventId);
        EventFullDto toReturn = eventMapper.toEventFullDto(event);
        toReturn.setConfirmedRequests(requestService.countEventConfirmedRequests(toReturn.getId()));
        return toReturn;
    }

    @Override
    public List<EventFullDto> getAllAdmin(List<Integer> users, List<State> states, List<Integer> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<User> userList = null;
        if (users != null) {
            userList = users.stream().map(element -> userMapper.fromUserDto(userService.getUserByIdOrThrow(element))).collect(Collectors.toList());
        }
        List<Category> categoryList = null;
        if (categories != null) {
            categoryList = categories.stream()
                    .map(element -> categoryMapper.fromCategoryDto(categoryService.getCategoryByIdOrThrow(element)))
                    .collect(Collectors.toList());
        }
        List<Event> toReturn = customEventRepository.getAllAdmin(userList, states, categoryList, rangeStart, rangeEnd, from, size);
        return toReturn.stream()
                .map(eventMapper::toEventFullDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getAllUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size) {

        List<Event> toReturn = customEventRepository.getAllUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return toReturn.stream()
                .map(eventMapper::toEventFullDto)
                .peek(element -> element.setConfirmedRequests(requestService.countEventConfirmedRequests(element.getId())))
                .collect(Collectors.toList());

    }

}
