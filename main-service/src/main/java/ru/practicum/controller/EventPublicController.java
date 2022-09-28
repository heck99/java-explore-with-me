package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.SortType;
import ru.practicum.client.Client;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewStatisticDto;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {

    private final EventService eventService;
    private final Client client;

    @GetMapping("/{eventId}")
    public EventFullDto getPublishedEvents(@PathVariable int eventId, HttpServletRequest request) {
        client.post("/hit",
                new NewStatisticDto("ewm", request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));
        return eventService.getPublishedEvent(eventId);
    }

    @GetMapping()
    public List<EventFullDto> getPublishedEvents(@RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                 @RequestParam(required = false) String text,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam SortType sort,
                                                 @RequestParam(required = false) List<Integer> categories,
                                                 @RequestParam(required = false, name = "rangeStart") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                 @RequestParam(required = false, name = "rangeEnd") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                 HttpServletRequest request) {
        client.post("/hit",
                new NewStatisticDto("ewm", request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));
        return eventService.getAllUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
