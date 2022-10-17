package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.NewRatingDto;
import ru.practicum.dto.RatingDto;
import ru.practicum.dto.UpdateEventRequest;
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
        return eventService.getEventByUserOrThrow(eventId, userId);
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
