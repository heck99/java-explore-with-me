package ru.practicum.service.impl.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.Location;
import ru.practicum.dto.NewEventDto;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.service.EventService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceIntegrationTest {

    private final EntityManager em;
    private final EventService service;

    @Test
    public void createCorrect() {
        TypedQuery<Event> query0 = em.createQuery("SELECT e FROM Event e", Event.class);
        List<Event> events = query0.getResultList();
        assertEquals(events.size(), 2);

        NewEventDto newEventDto = new NewEventDto("annotation1", 1, "description1",
                LocalDateTime.now().plusHours(4), new Location(100.2, 200.1), true, 0,
                true, "title1");
        EventFullDto event = service.createEvent(newEventDto, 1);
        assertEquals(event.getId(), 3);
        assertEquals(event.getAnnotation(), "annotation1");
        assertEquals(event.getDescription(), "description1");
        assertEquals(event.getTitle(), "title1");
        assertTrue(event.getRequestModeration());
        assertTrue(event.getPaid());
        assertEquals(event.getLocation().getLat(), 100.2);
        assertEquals(event.getLocation().getLon(), 200.1);
        assertEquals(event.getCategory().getId(), 1);
        assertEquals(event.getCategory().getName(), "category1");
        assertTrue(event.getEventDate().minusHours(4).isBefore(LocalDateTime.now()));
        assertEquals(event.getInitiator().getId(), 1);
        assertEquals(event.getInitiator().getName(), "user1");
        assertTrue(event.getCreatedOn().isBefore(LocalDateTime.now()));
        assertEquals(event.getState(), State.PENDING);

        events = query0.getResultList();
        assertEquals(events.size(), 3);
    }
}
