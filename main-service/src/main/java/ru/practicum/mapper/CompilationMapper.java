package ru.practicum.mapper;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.stream.Collectors;

public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        EventMapper mapper = new EventMapper();
        return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(),
                compilation.getEvents().stream().map(mapper::toEventShortDto).collect(Collectors.toSet()));
    }

    public Compilation fromNewCompilationDto(NewCompilationDto dto) {
        return new Compilation(null, dto.getPinned(), dto.getTitle(), dto.getEvents().stream()
                .map(element -> {
                    Event event = new Event();
                    event.setId(element);
                    return event;
                }).collect(Collectors.toSet()));
    }
}
