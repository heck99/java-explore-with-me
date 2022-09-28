package ru.practicum.dto;


import lombok.*;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CompilationDto {

    private Integer id;

    private Boolean pinned;

    private String title;

    private Set<EventShortDto> events;
}
