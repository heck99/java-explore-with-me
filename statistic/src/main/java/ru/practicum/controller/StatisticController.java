package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.service.StatisticService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService service;

    @PostMapping("/hit")
    void createStatistic(@Valid @RequestBody NewStatisticDto statisticDto) {
        service.createStatistic(statisticDto);
    }

    @GetMapping("/stats")
    List<StatisticDto> getStatistic(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return service.getStatistic(start, end, uris, unique);
    }
}
