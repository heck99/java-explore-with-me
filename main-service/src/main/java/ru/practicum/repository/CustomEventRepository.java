package ru.practicum.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.practicum.SortType;
import ru.practicum.model.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomEventRepository {

    private final EntityManager entityManager;


    public List<Event> getAllAdmin(List<User> users, List<State> states, List<Category> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = cb.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (users != null) {
            predicates.add(cb.in(root.get("initiator")).value(users));
        }
        if (states != null) {
            predicates.add(cb.in(root.get("state")).value(states));
        }
        if (categories != null) {
            predicates.add(cb.in(root.get("category")).value(categories));
        }
        if (rangeStart != null) {
            predicates.add(cb.greaterThan(root.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(cb.lessThan(root.get("eventDate"), rangeEnd));
        }
        Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(finalPredicate);
        return entityManager.createQuery(criteriaQuery).setMaxResults(size).setFirstResult(from).getResultList();
    }

    public List<Event> getAllUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size) {

        StringBuilder query = new StringBuilder("SELECT e.* FROM events e ");
        if (onlyAvailable) {
            query.append("LEFT JOIN participation_request p_r ON e.event_id = p_r.event_id AND p_r.status = 'CONFIRMED' ");
        }
        query.append("WHERE TRUE ");
        if (text != null) {
            query.append("AND (e.annotation like '%").append(text).append("%' OR description like '%").append(text).append("%' )");
        }

        if (categories != null) {
            query.append("AND e.category_id IN ").append("(");
            categories.forEach(element -> query.append(element).append(", "));
            query.deleteCharAt(query.length() - 2).append(") ");
        }

        if (paid != null) {
            query.append("AND e.paid = ").append(paid).append(" ");
        }

        if (rangeStart != null) {
            query.append("AND e.event_date > '").append(rangeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))).append("' ");
        }

        if (rangeEnd != null) {
            query.append("AND e.event_date < '").append(rangeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))).append("' ");
        }

        if (rangeEnd == null && rangeStart == null) {
            query.append("AND e.event_date < '").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))).append("' ");
        }

        if (onlyAvailable) {
            query.append("GROUP BY e.event_id ");
            query.append("HAVING count(p_r.participation_request_id) < e.participant_limit ");
        }

        switch (sort) {
            case VIEWS:
                query.append("ORDER BY e.event_date ");
                break;
            case EVENT_DATE:
            default:
                query.append("ORDER BY e.views ");
        }
        return entityManager.createNativeQuery(query.toString(), Event.class).setMaxResults(size).setFirstResult(from).getResultList();
    }
}
