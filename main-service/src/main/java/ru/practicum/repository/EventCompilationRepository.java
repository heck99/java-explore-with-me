package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventCompilation;

import java.util.Optional;

@Repository
public interface EventCompilationRepository extends JpaRepository<EventCompilation, Integer> {

    Optional<EventCompilation> findByEventIdAndCompilationId(int eventId, int compId);

}
