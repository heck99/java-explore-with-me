package ru.practicum.service;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.Optional;

public interface RequestServiceFull extends RequestService {
    int countEventConfirmedRequests(int eventId);

    Optional<ParticipationRequestDto> getRequestByEventAndUser(int eventId, int userId);
}
