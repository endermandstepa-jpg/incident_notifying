package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.*;
import com.emergency.alert.storage.InMemoryDatabase;
import com.emergency.alert.telegram.EmergencyTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyEventService {

    private final EmergencyTelegramBot bot;
    private final GeoService geoService;

    public EmergencyEvent create(CreateEventRequest request) {

        try {
            if (request == null || request.getTitle() == null || request.getMessageText() == null) {
                log.warn("Invalid request: {}", request);
                return null;
            }

            long eventId = InMemoryDatabase.EVENT_SEQ.getAndIncrement();

            EmergencyEvent event = EmergencyEvent.builder()
                    .id(eventId)
                    .title(request.getTitle())
                    .messageText(request.getMessageText())
                    .priority(request.getPriority())
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .build();

            InMemoryDatabase.EVENTS.put(eventId, event);

            long zoneId = InMemoryDatabase.ZONE_SEQ.getAndIncrement();

            GeoZone zone = GeoZone.builder()
                    .id(zoneId)
                    .eventId(eventId)
                    .city(request.getCity())
                    .centerLat(request.getCenterLat())
                    .centerLng(request.getCenterLng())
                    .radiusKm(request.getRadiusKm())
                    .build();

            InMemoryDatabase.ZONES.put(zoneId, zone);

            int notified = 0;

            for (User user : InMemoryDatabase.USERS.values()) {

                try {
                    if (user.getLatitude() == null || user.getLongitude() == null) continue;

                    boolean inside = geoService.insideRadius(
                            user.getLatitude(),
                            user.getLongitude(),
                            zone.getCenterLat(),
                            zone.getCenterLng(),
                            zone.getRadiusKm()
                    );

                    if (!inside) continue;

                    boolean sent = false;

                    try {
                        sent = bot.sendEmergency(
                                user.getMessengerId(),
                                event.getTitle(),
                                event.getMessageText()
                        );
                    } catch (Exception e) {
                        log.error("Telegram failed", e);
                    }

                    long notifId = InMemoryDatabase.NOTIF_SEQ.getAndIncrement();

                    Notification n = Notification.builder()
                            .id(notifId)
                            .eventId(eventId)
                            .userId(user.getId())
                            .deliveryStatus(sent ? "SENT" : "FAILED")
                            .sentAt(LocalDateTime.now())
                            .build();

                    InMemoryDatabase.NOTIFICATIONS.put(notifId, n);
                    notified++;

                } catch (Exception e) {
                    log.error("User processing failed", e);
                }
            }

            log.info("EVENT CREATED: {} | notified={}", eventId, notified);

            return event;

        } catch (Exception e) {
            log.error("EVENT CREATION FAILED", e);
            return null;
        }
    }
}