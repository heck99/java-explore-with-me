package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventRequest {

    @NotNull
    private Integer eventId;

    private String annotation;

    private CategoryDto category;

    private String description;

    private LocalDate eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private String title;
}
