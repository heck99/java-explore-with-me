package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.SortType;
import ru.practicum.dto.EventFullDto;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {

    private final EventService eventService;


    @GetMapping("/{eventId}")
    public EventFullDto getPublishedEvents(@PathVariable int eventId) {
        return eventService.getPublishedEvent(eventId);
    }

    @GetMapping("")
    public List<EventFullDto> getPublishedEvents(@RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                 @RequestParam(required = false) String text,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam SortType sort,
                                                 @RequestParam(required = false) List<Integer> categories,
                                                 @RequestParam(required = false, name = "rangeStart") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                 @RequestParam(required = false, name = "rangeEnd") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd) {
        return eventService.getAllUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
