package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping
    public EventFullDto createEvent(@RequestBody @Valid NewEventDto event, @PathVariable int userId) {
        return eventService.createEvent(event, userId);
    }

    @PatchMapping
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventRequest event, @PathVariable int userId) {
        return eventService.updateEvent(event, userId);
    }

    @GetMapping
    public List<EventShortDto> getAllUsersEvents(@PathVariable Integer userId,
                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getAllUsersEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getAllUsersEvents(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getEventByUser(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.cancelEvent(eventId, userId);
    }

    @PostMapping("/{eventId}/ratings")
    public RatingDto createRating(@PathVariable Integer userId, @PathVariable Integer eventId,
                                  @Valid @RequestBody NewRatingDto rating) {
        return eventService.createRating(userId, eventId, rating);
    }

    @PatchMapping("/{eventId}/ratings")
    public RatingDto updateRating(@PathVariable Integer userId, @PathVariable Integer eventId,
                                  @RequestBody NewRatingDto rating) {
        return eventService.updateRating(userId, eventId, rating);
    }
}
