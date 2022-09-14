package ru.practicum.mapper;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;

public class ParticipationMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getId(), request.getEvent().getId(), request.getRequester().getId(),
                request.getStatus(), request.getCreated());
    }

    public ParticipationRequest fromParticipationRequestDto(ParticipationRequestDto dto) {
        Event event = new Event();
        event.setId(dto.getEvent());
        return new ParticipationRequest(dto.getId(), event, new User(dto.getRequester(), null, null),
                dto.getStatus(), dto.getCreated());
    }
}
