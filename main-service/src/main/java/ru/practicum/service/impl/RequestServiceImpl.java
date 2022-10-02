package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.IncorrectParametersException;
import ru.practicum.exception.NoAccessException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.RequestState;
import ru.practicum.model.State;
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

    private final ParticipationRequestMapper requestMapper;
    private final UserServiceFull userService;
    private final EventServiceFull eventService;

    private final UserMapper userMapper;

    private final EventMapper eventMapper;

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        Optional<ParticipationRequest> request = requestRepository.findByEventIdAndRequesterId(eventId, userId);
        if (request.isPresent()) {
            throw new IncorrectParametersException(String.format("Запрос на участие в событие с id %d от пользователя с id %d уже существует",
                    eventId, userId));
        }

        EventFullDto event = eventService.getEventByIdOrThrow(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new NoAccessException("Нельзя учвствовать в неопубликованном событии");
        }

        if (event.getInitiator().getId() == userId) {
            throw new IncorrectParametersException("Запрос на участие в событие не может быть отправлен от создателя события");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new NoAccessException("Превышен лимит запросов на событие");
        }

        UserDto user = userService.getUserByIdOrThrow(userId);
        ParticipationRequest newRequest;
        if (event.getRequestModeration()) {
            newRequest = new ParticipationRequest(null, eventMapper.fromEventFullDto(event), userMapper.fromUserDto(user),
                    RequestState.PENDING, LocalDateTime.now());
        } else {
            newRequest = new ParticipationRequest(null, eventMapper.fromEventFullDto(event), userMapper.fromUserDto(user),
                    RequestState.CONFIRMED, LocalDateTime.now());
        }
        return requestMapper.toParticipationRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public List<ParticipationRequestDto> getAllByRequester(int userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("запрос с id = %d не найден", requestId)));

        if (userId != request.getRequester().getId()) {
            throw new NoAccessException("Только создатель запроса может отменить его");
        }
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccessException("Нельзя отменить запрос, который не находится в статусе ожидания подтверждения");
        }
        if (request.getEvent().getParticipantLimit() > 0) {
            int count = requestRepository.countAllByEventIdAndStatus(requestId, RequestState.CONFIRMED);
            if (request.getEvent().getParticipantLimit() <= count) {
                throw new NoAccessException("Превышен лимит запросов на событие");
            }
        }
        request.setStatus(RequestState.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto confirmRequest(int userId, int requestId, int eventId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("запрос с id = %d не найден", requestId)));
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccessException("Нельзя подтвердить запрос, который не находится в статусе ожидания подтверждения");
        }
        if (request.getEvent().getInitiator().getId() != userId) {
            throw new NoAccessException("Только организатор события может подтвердить запрос");
        }

        if (request.getEvent().getId() != eventId) {
            throw new IncorrectParametersException("eventId не соответствует id события для которого создан запрос с requestId");
        }
        int count = requestRepository.countAllByEventIdAndStatus(eventId, RequestState.CONFIRMED);

        if (request.getEvent().getParticipantLimit() <= count) {
            throw new NoAccessException("Превышен лимит запросов на событие");
        }
        request.setStatus(RequestState.CONFIRMED);
        request = requestRepository.save(request);

        if (count + 1 == request.getEvent().getParticipantLimit()) {
            requestRepository.cancelAllRequests(eventId);
        }
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto rejectRequest(int userId, int requestId, int eventId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("запрос с id = %d не найден", requestId)));
        if (request.getEvent().getInitiator().getId() != userId) {
            throw new NoAccessException("Только организатор события может отклонить запрос");
        }

        if (request.getEvent().getId() != eventId) {
            throw new IncorrectParametersException("eventId не соответствует id события для которого создан запрос с requestId");
        }
        if (request.getStatus() != RequestState.PENDING) {
            throw new NoAccessException("Нельзя отменить запрос, который не находится в статусе ожидания подтверждения");
        }
        request.setStatus(RequestState.REJECTED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByEventId(int eventId, int userId) {
        EventFullDto event = eventService.getPublishedEventOrThrow(eventId);

        if (userId != event.getInitiator().getId()) {
            throw new NoAccessException("Только организатор имеет доступ к этому событию");
        }
        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public int countEventConfirmedRequests(int eventId) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestState.CONFIRMED);
    }

    @Override
    public Optional<ParticipationRequestDto> getRequestByEventAndUser(int eventId, int userId) {
        return requestRepository.findByEventIdAndRequesterId(eventId, userId)
                .map(participationRequestMapper::toParticipationRequestDto);
    }
}
