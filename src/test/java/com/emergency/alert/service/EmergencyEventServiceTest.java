package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.repository.*;
import com.emergency.alert.telegram.EmergencyTelegramBot;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyEventServiceTest {

    private final EmergencyEventRepository eventRepo = mock(EmergencyEventRepository.class);
    private final GeoZoneRepository geoRepo = mock(GeoZoneRepository.class);
    private final UserRepository userRepo = mock(UserRepository.class);
    private final NotificationRepository notificationRepo = mock(NotificationRepository.class);
    private final EmergencyTelegramBot bot = mock(EmergencyTelegramBot.class);
    private final GeoService geoService = new GeoService();

    private final EmergencyEventService service =
            new EmergencyEventService(eventRepo, geoRepo, userRepo, notificationRepo, bot, geoService);

    @Test
    void shouldCreateEvent() {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test");
        request.setMessageText("Message");
        request.setCity("Berlin");

        EmergencyEvent saved = new EmergencyEvent();
        saved.setTitle("Test");

        when(eventRepo.save(any())).thenReturn(saved);
        when(userRepo.findAll()).thenReturn(Collections.emptyList());

        EmergencyEvent result = service.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}