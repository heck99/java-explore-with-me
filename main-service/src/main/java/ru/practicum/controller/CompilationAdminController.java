package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }


    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable int compId, @PathVariable int eventId) {
        compilationService.deleteEventFromCompilation(eventId, compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId, @PathVariable int eventId) {
        compilationService.addEventToCompilation(eventId, compId);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable int compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable int compId) {
        compilationService.pinCompilation(compId);
    }

}
