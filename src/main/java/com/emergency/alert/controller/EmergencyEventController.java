package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.dto.EventInfoResponse;
import com.emergency.alert.dto.EventResponseStatusDto;
import com.emergency.alert.entity.*;
import com.emergency.alert.repository.*;
import com.emergency.alert.service.EmergencyEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EmergencyEventController {

    private final EmergencyEventService service;
    private final EmergencyEventRepository eventRepository;
    private final GeoZoneRepository geoZoneRepository;
    private final NotificationRepository notificationRepository;
    private final UserResponseRepository userResponseRepository;
    private final UserRepository userRepository;

    @PostMapping
    public EmergencyEvent create(@RequestBody CreateEventRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<EventInfoResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> {

            GeoZone zone = geoZoneRepository.findByEventId(event.getId()).orElse(null);

            EventInfoResponse dto = new EventInfoResponse();
            dto.setEventId(event.getId());
            dto.setCreatedAt(event.getCreatedAt());

            if (zone != null) {
                dto.setCenterLat(zone.getCenterLat());
                dto.setCenterLng(zone.getCenterLng());
                dto.setRadiusKm(zone.getRadiusKm());
            }

            return dto;
        }).toList();
    }

    @GetMapping("/{eventId}/responses")
    public List<EventResponseStatusDto> getResponses(@PathVariable Long eventId) {

        List<Notification> notifications =
                notificationRepository.findAll().stream()
                        .filter(n -> n.getEventId().equals(eventId))
                        .toList();

        List<Long> ids = notifications.stream()
                .map(Notification::getId)
                .toList();

        List<UserResponse> responses =
                userResponseRepository.findAll().stream()
                        .filter(r -> ids.contains(r.getNotificationId()))
                        .toList();

        return responses.stream().map(r -> {

            User user = userRepository.findById(r.getUserId()).orElseThrow();

            EventResponseStatusDto dto = new EventResponseStatusDto();
            dto.setUserId(user.getId());
            dto.setUserName(user.getFullName());
            dto.setResponseType(r.getResponseType());

            return dto;
        }).toList();
    }
}