package ru.practicum.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(int compId);

    void deleteEventFromCompilation(int eventId, int compId);

    void addEventToCompilation(int eventId, int compId);

    void unpinCompilation(int compId);

    void pinCompilation(int compId);

    CompilationDto getCompilationById(int compId);

    List<CompilationDto> getAllCompilations(Boolean pinned, int size, int from);
}
