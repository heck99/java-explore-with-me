package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByEventIdAndUserId(int eventId, int userId);


    @Query("SELECT (sum(r.mark) + 0.0) /count(r.mark) FROM Rating r WHERE r.event.initiator.id = :userId")
    Double getAVGUserRating(int userId);

    List<Rating> findAllByEventId(int eventId, Pageable page);

    @Query("SELECT (sum(r.mark) + 0.0) /count(r.mark) FROM Rating r WHERE r.event.id = :eventId")
    Double getEventRating(int eventId);
}
