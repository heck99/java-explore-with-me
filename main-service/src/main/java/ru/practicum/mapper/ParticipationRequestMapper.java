package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

@Component
public class ParticipationRequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
