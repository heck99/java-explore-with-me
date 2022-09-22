package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;


import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByInitiatorId(int id, Pageable page);

/*    @Query("select e FROM Event e " +
            "WHERE case  when :users IS NULL then TRUE else (e.initiator.id IN :users) end " +
            "AND case when (:states IS NULL) then TRUE else (e.state IN :states) end " +
            "AND case when (:categories IS NULL) then TRUE else (e.category.id IN :categories) end " +
            "AND case when (:rangeStart IS NULL) then TRUE else (e.eventDate > :rangeStart) end " +
            "AND case when (:rangeEnd IS NULL) then TRUE else (e.eventDate < :rangeEnd) end ")
    List<Event> searchAdmin(List<Integer> users, List<State> states, List<Integer> categories,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);*/

    /*    @Query("select e FROM Event e " +
                "WHERE case when (:categories IS NULL) then TRUE else (e.category.id IN :categories) end " +
                "AND case when (:paid IS NULL) then TRUE else (e.paid = :paid) end " +
                "AND case when (:rangeStart IS NULL) then TRUE else (e.eventDate > :rangeStart) end " +
                "AND case when (:rangeEnd IS NULL) then TRUE else (e.eventDate < :rangeEnd) end " +
                "AND case when (:text IS NULL) then TRUE else (e.annotation like %:text%) end " +
                "AND e.state = 'PUBLISHED'")
        List<Event> searchUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable page);*/
    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.views = e.views + 1 WHERE e.id =:eventId")
    void addView(int eventId);
}
