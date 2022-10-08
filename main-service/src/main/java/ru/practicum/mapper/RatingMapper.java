package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.NewRatingDto;
import ru.practicum.dto.RatingDto;
import ru.practicum.model.Rating;

@Component
public class RatingMapper {

    public Rating fromNewRatingDto(NewRatingDto dto) {
        return new Rating(null, dto.getDescription(), dto.getMark(), null, null);
    }

    public RatingDto toRatingDto(Rating rating) {
        return new RatingDto(rating.getId(), rating.getDescription(), rating.getMark(), rating.getUserId());
    }
}
