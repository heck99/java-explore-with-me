package ru.practicum.mapper;

import ru.practicum.dto.NewStatisticDto;
import ru.practicum.model.Statistic;

public class StatisticMapper {
    public Statistic fromNewStatisticDto(NewStatisticDto dto) {
        return new Statistic(null, dto.getApp(), dto.getUrl(), dto.getIp(), dto.getTime());
    }
}
