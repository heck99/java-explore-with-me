package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank
    @Size(max = 7000, min = 20)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;


    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    private State state;

    @NotNull
    @Size(max = 120, min = 3)
    private String title;
}
