package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.RequestState;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    Optional<ParticipationRequest> findByEventIdAndRequesterId(int eventId, int userId);

    List<ParticipationRequest> findAllByRequesterId(int userId);

    int countAllByEventIdAndStatus(int eventId, RequestState state);

    @Query("UPDATE ParticipationRequest SET status = 'CANCEL' WHERE event.id = :eventId AND status = 'PENDING'")
    int cancelAllRequests(int eventId);

    List<ParticipationRequest> findAllByEventId(int eventId);

}
