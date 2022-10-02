package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/requests")
@AllArgsConstructor
public class EventRequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAllByRequester(@PathVariable int userId, @PathVariable int eventId) {
        return requestService.getAllRequestsByEventId(eventId, userId);
    }

    @PatchMapping("/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable int userId, @PathVariable int eventId, @PathVariable int reqId) {
        return requestService.confirmRequest(userId, reqId, eventId);
    }

    @PatchMapping("/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable int userId, @PathVariable int eventId, @PathVariable int reqId) {
        return requestService.rejectRequest(userId, reqId, eventId);
    }
}
