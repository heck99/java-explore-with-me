package ru.practicum.service;

import ru.practicum.model.ParticipationRequest;

import java.util.Optional;

public interface RequestServiceFull extends RequestService {
    int countEventConfirmedRequests(int eventId);

    Optional<ParticipationRequest> getRequestByEventAndUser(int eventId, int userId);
}
