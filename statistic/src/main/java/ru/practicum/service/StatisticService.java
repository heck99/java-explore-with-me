package ru.practicum.service;

import ru.practicum.dto.NewStatisticDto;
import ru.practicum.dto.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    void createStatistic(NewStatisticDto statisticDto);

    List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
