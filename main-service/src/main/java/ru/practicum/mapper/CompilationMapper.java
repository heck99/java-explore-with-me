package ru.practicum.mapper;

import ru.practicum.dto.CompilationDto;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        EventMapper mapper = new EventMapper();
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(mapper::toEventShortDto).collect(Collectors.toSet()))
                .build();
    }
}
