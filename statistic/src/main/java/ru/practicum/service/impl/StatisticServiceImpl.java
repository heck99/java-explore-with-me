package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewStatisticDto;
import ru.practicum.dto.StatisticDto;
import ru.practicum.mapper.StatisticMapper;
import ru.practicum.model.Statistic;
import ru.practicum.repository.StatisticRepository;
import ru.practicum.service.StatisticService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository repository;
    private final StatisticMapper sm = new StatisticMapper();

    @Override
    public void createStatistic(NewStatisticDto statisticDto) {
        Statistic statistic = sm.fromNewStatisticDto(statisticDto);
        repository.save(statistic);
    }

    @Override
    public List<StatisticDto> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Statistic> result;
        List<StatisticDto> toReturn = new ArrayList<>();
        for (String uri : uris) {
            if (unique) {
                result = repository.findWithUniqueIp(start, end, uri);
            } else {
                result = repository.findAllByTimeAfterAndTimeBeforeAndUrl(start, end, uri);
            }
            if (result.size() != 0) {
                toReturn.add(new StatisticDto(result.get(0).getApp(), result.get(0).getUrl(), result.size()));
            } else {
                toReturn.add(new StatisticDto(null, uri, 0));
            }
        }
        return toReturn;
    }
}
