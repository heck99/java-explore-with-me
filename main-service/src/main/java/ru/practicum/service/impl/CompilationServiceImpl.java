package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.exception.IncorrectParameters;
import ru.practicum.exception.NotFound;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.EventCompilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventCompilationRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventServiceFull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventServiceFull eventService;

    private final EventMapper eventMapper = new EventMapper();

    private final CompilationMapper compilationMapper = new CompilationMapper();

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Set<Event> events = compilationDto.getEvents().stream()
                .map(element -> eventMapper.fromEventFullDto(eventService.getEventById(element))).collect(Collectors.toSet());


        Compilation compilation = new Compilation(null, compilationDto.getPinned(), compilationDto.getTitle(), events);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFound(String.format("подборка с id = %d не найден", compId)));
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(int eventId, int compId) {
        EventCompilation eventCompilation = eventCompilationRepository.findByEventIdAndCompilationId(eventId, compId)
                .orElseThrow(() -> new NotFound(String.format("В подборке с id = %d не найдено событие с id = %d", compId, eventId)));
        eventCompilationRepository.deleteById(eventCompilation.getId());
    }

    @Override
    public void addEventToCompilation(int eventId, int compId) {
        EventCompilation eventCompilation = new EventCompilation(null, eventMapper.fromEventFullDto(eventService.getEventById(eventId)),
                compilationRepository.findById(compId)
                        .orElseThrow(() -> new NotFound(String.format("подборка с id = %d не найден", compId))));
        eventCompilationRepository.save(eventCompilation);
    }

    @Override
    public void unpinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFound(String.format("подборка с id = %d не найден", compId)));
        if (!compilation.getPinned()) {
            throw new IncorrectParameters("Подборка уже откреплена");
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFound(String.format("подборка с id = %d не найден", compId)));
        if (compilation.getPinned()) {
            throw new IncorrectParameters("Подборка уже закреплена");
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto getCompilationById(int compId) {
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFound(String.format("подборка с id = %d не найден", compId))));
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int size, int from) {
        Pageable page = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(page).stream().map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
        Example<Compilation> example = Example.of(new Compilation(null, pinned, null, null));
        return compilationRepository.findAll(example, page).stream().map(compilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}
