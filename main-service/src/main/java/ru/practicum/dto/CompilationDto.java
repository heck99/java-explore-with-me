package ru.practicum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CompilationDto {

    private Integer id;

    private Boolean pinned;

    private String title;

    private Set<EventShortDto> events;
}
