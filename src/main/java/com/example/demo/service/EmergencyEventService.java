package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.*;

import com.emergency.alert.repository.*;

import com.emergency.alert.telegram.EmergencyTelegramBot;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyEventService {

    private final EmergencyEventRepository eventRepository;
    private final GeoZoneRepository geoZoneRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmergencyTelegramBot bot;
    private final GeoService geoService;

    public EmergencyEvent create(
            CreateEventRequest request
    ) {

        EmergencyEvent event =
                EmergencyEvent.builder()
                        .title(request.getTitle())
                        .messageText(request.getMessageText())
                        .priority(request.getPriority())
                        .status("ACTIVE")
                        .createdAt(Instant.now())
                        .build();

        eventRepository.save(event);

        GeoZone zone =
                GeoZone.builder()
                        .eventId(event.getEventId())
                        .city(request.getCity())
                        .centerLat(request.getCenterLat())
                        .centerLng(request.getCenterLng())
                        .radiusKm(request.getRadiusKm())
                        .build();

        geoZoneRepository.save(zone);

        List<User> users = userRepository.findAll();

        for (User user : users) {

            boolean inside = geoService.insideRadius(
                    user.getLatitude(),
                    user.getLongitude(),
                    zone.getCenterLat(),
                    zone.getCenterLng(),
                    zone.getRadiusKm()
            );

            if (!inside) {
                continue;
            }

            boolean sent = bot.sendEmergency(
                    user.getMessengerId(),
                    event.getTitle(),
                    event.getMessageText()
            );

            notificationRepository.save(
                    Notification.builder()
                            .eventId(event.getEventId())
                            .userId(user.getUserId())
                            .sentAt(Instant.now())
                            .deliveryStatus(
                                    sent ? "SENT" : "FAILED")
                            .build()
            );
        }

        return event;
    }
}