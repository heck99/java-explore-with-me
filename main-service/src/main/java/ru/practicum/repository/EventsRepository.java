package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

@Repository
public interface EventsRepository extends JpaRepository<Event, Integer> {
}
