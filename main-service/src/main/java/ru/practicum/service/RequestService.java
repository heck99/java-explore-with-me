package ru.practicum.service;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(int userId, int eventId);

    List<ParticipationRequestDto> getAllByRequester(int userId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    ParticipationRequestDto confirmRequest(int userId, int requestId, int eventId);

    ParticipationRequestDto rejectRequest(int userId, int requestId, int eventId);

    List<ParticipationRequestDto> getAllRequestsByEventId(int eventId, int userId);
}
