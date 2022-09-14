package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.RequestState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParticipationRequestDto {
    private Integer id;

    private Integer event;

    private Integer requester;

    private RequestState status;

    private LocalDateTime created;
}
