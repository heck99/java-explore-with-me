package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                                   @RequestParam(required = false) Boolean pinned) {
        return compilationService.getAllCompilations(pinned, size, from);
    }

    @GetMapping("{compId}")
    public CompilationDto getAllCompilations(@PathVariable int compId) {
        return compilationService.getCompilationById(compId);
    }

}
