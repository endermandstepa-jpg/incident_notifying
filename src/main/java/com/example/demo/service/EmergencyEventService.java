package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.*;
import com.emergency.alert.repository.*;
import com.emergency.alert.telegram.EmergencyTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyEventService {

    private final EmergencyEventRepository eventRepository;
    private final GeoZoneRepository geoZoneRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmergencyTelegramBot bot;
    private final GeoService geoService;

    @Transactional
    public EmergencyEvent create(CreateEventRequest request) {
        
        // Сохраняем событие
        EmergencyEvent event = EmergencyEvent.builder()
                .title(request.getTitle())
                .messageText(request.getMessageText())
                .priority(request.getPriority())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        
        event = eventRepository.save(event);
        
        // Сохраняем гео-зону
        GeoZone zone = GeoZone.builder()
                .eventId(event.getEventId())
                .city(request.getCity())
                .centerLat(request.getCenterLat())
                .centerLng(request.getCenterLng())
                .radiusKm(request.getRadiusKm())
                .build();
        
        geoZoneRepository.save(zone);
        
        // Отправляем уведомления пользователям в зоне
        List<User> users = userRepository.findAll();
        int notifiedCount = 0;
        
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
            
            Notification notification = Notification.builder()
                    .eventId(event.getEventId())
                    .userId(user.getUserId())
                    .deliveryStatus(sent ? "SENT" : "FAILED")
                    .sentAt(LocalDateTime.now())
                    .build();
            
            notificationRepository.save(notification);
            notifiedCount++;
        }
        
        log.info("Event {} created. Notified {} users", event.getEventId(), notifiedCount);
        
        return event;
    }
}