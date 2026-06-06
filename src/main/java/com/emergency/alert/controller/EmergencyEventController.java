package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.dto.EventInfoResponse;
import com.emergency.alert.dto.EventResponseStatusDto;
import com.emergency.alert.entity.*;
import com.emergency.alert.storage.InMemoryDatabase;
import com.emergency.alert.service.EmergencyEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EmergencyEventController {

    private final EmergencyEventService service;

    @PostMapping
    public EmergencyEvent create(@RequestBody CreateEventRequest request) {
        try {
            return service.create(request);
        } catch (Exception e) {
            log.error("CREATE EVENT FAILED", e);
            return null;
        }
    }

    @GetMapping
    public List<EventInfoResponse> getAllEvents() {

        return InMemoryDatabase.EVENTS.values()
                .stream()
                .map(event -> {

                    GeoZone zone = InMemoryDatabase.ZONES.values()
                            .stream()
                            .filter(z -> z.getEventId().equals(event.getId()))
                            .findFirst()
                            .orElse(null);

                    EventInfoResponse dto = new EventInfoResponse();
                    dto.setEventId(event.getId());
                    dto.setTitle(event.getTitle());
                    dto.setMessageText(event.getMessageText());
                    dto.setCreatedAt(event.getCreatedAt());

                    if (zone != null) {
                        dto.setCenterLat(zone.getCenterLat());
                        dto.setCenterLng(zone.getCenterLng());
                        dto.setRadiusKm(zone.getRadiusKm());
                    }

                    return dto;
                })
                .toList();
    }

    @GetMapping("/{eventId}/responses")
    public List<EventResponseStatusDto> getResponses(@PathVariable Long eventId) {

        List<Long> notificationIds = InMemoryDatabase.NOTIFICATIONS.values()
                .stream()
                .filter(n -> n.getEventId().equals(eventId))
                .map(Notification::getId)
                .toList();

        return InMemoryDatabase.RESPONSES.values()
                .stream()
                .filter(r -> notificationIds.contains(r.getNotificationId()))
                .map(r -> {

                    User user = InMemoryDatabase.USERS.get(r.getUserId());

                    EventResponseStatusDto dto = new EventResponseStatusDto();
                    dto.setUserId(r.getUserId());
                    dto.setUserName(user != null ? user.getFullName() : "unknown");
                    dto.setResponseType(r.getResponseType());

                    return dto;
                })
                .toList();
    }
}