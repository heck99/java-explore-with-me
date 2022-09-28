package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.IncorrectParameters;
import ru.practicum.exception.NoAccess;
import ru.practicum.exception.NotFound;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.*;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.service.EventServiceFull;
import ru.practicum.service.RequestServiceFull;
import ru.practicum.service.UserServiceFull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestServiceFull {

    private final ParticipationRequestRepository requestRepository;

    private final ParticipationRequestMapper rm = new ParticipationRequestMapper();
    private final UserServiceFull userService;
    private final EventServiceFull eventService;

    private final UserMapper userMapper = new UserMapper();

    private final EventMapper eventMapper = new EventMapper();

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        Optional<ParticipationRequest> request = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request.isPresent()) {
            throw new IncorrectParameters(String.format("Запрос на участие в событие с id %d от пользователя с id %d уже существует",
                    eventId, userId));
        }

        EventFullDto event = eventService.getEventById(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new NoAccess("Нельзя учвствовать в неопубликованном событии");
        }

        if (event.getInitiator().getId() == userId) {
            throw new IncorrectParameters("Запрос на участие в событие не может быть отправлен от создателя события");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new NoAccess("Превышен лимит запросов на событие");
        }

        UserDto user = userService.getUserById(userId);
        ParticipationRequest newRequest;
        if (event.getRequestModeration()) {
            newRequest = new ParticipationRequest(null, eventMapper.fromEventFullDto(event), userMapper.fromUserDto(user),
                    RequestState.PENDING, LocalDateTime.now());
        } else {
            newRequest = new ParticipationRequest(null, eventMapper.fromEventFullDto(event), userMapper.fromUserDto(user),
                    RequestState.CONFIRMED, LocalDateTime.now());
        }
        return rm.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public List<ParticipationRequestDto> getAllByRequester(int userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(rm::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFound(String.format("запрос с id = %d не найден", requestId)));

        if (userId != request.getRequester().getId()) {
            throw new NoAccess("Только создатель запроса может отменить его");
        }
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccess("Нельзя отменить запрос, который не находится в статусе ожидания подтверждения");
        }
        if (request.getEvent().getParticipantLimit() > 0) {
            int count = requestRepository.countAllByEventIdAndStatus(requestId, RequestState.CONFIRMED);
            if (request.getEvent().getParticipantLimit() <= count) {
                throw new NoAccess("Превышен лимит запросов на событие");
            }
        }
        request.setStatus(RequestState.CANCELED);
        return rm.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto confirmRequest(int userId, int requestId, int eventId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFound(String.format("запрос с id = %d не найден", requestId)));
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccess("Нельзя подтвердить запрос, который не находится в статусе ожидания подтверждения");
        }
        if (request.getEvent().getInitiator().getId() != userId) {
            throw new NoAccess("Только организатор события может подтвердить запрос");
        }

        if (request.getEvent().getId() != eventId) {
            throw new IncorrectParameters("eventId не соответствует id события для которого создан запрос с requestId");
        }
        int count = requestRepository.countAllByEventIdAndStatus(eventId, RequestState.CONFIRMED);

        if (request.getEvent().getParticipantLimit() <= count) {
            throw new NoAccess("Превышен лимит запросов на событие");
        }
        request.setStatus(RequestState.CONFIRMED);
        request = requestRepository.save(request);

        if (count + 1 == request.getEvent().getParticipantLimit()) {
            requestRepository.cancelAllRequests(eventId);
        }
        return rm.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto rejectRequest(int userId, int requestId, int eventId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFound(String.format("запрос с id = %d не найден", requestId)));
        if (request.getEvent().getInitiator().getId() != userId) {
            throw new NoAccess("Только организатор события может отклонить запрос");
        }

        if (request.getEvent().getId() != eventId) {
            throw new IncorrectParameters("eventId не соответствует id события для которого создан запрос с requestId");
        }
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccess("Нельзя отменить запрос, который не находится в статусе ожидания подтверждения");
        }
        request.setStatus(RequestState.REJECTED);
        return rm.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByEventId(int eventId, int userId) {
        EventFullDto event = eventService.getPublishedEvent(eventId);

        if (userId != event.getInitiator().getId()) {
            throw new NoAccess("Только организатор имеет доступ к этому событию");
        }
        return requestRepository.findAllByEventId(eventId).stream().map(rm::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public int countEventConfirmedRequests(int eventId) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestState.CONFIRMED);
    }
}
