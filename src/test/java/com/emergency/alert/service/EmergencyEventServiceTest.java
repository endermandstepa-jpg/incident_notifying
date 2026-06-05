package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.repository.*;
import com.emergency.alert.telegram.EmergencyTelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyEventServiceTest {

    private final EmergencyEventRepository eventRepo = Mockito.mock(EmergencyEventRepository.class);
    private final GeoZoneRepository geoRepo = Mockito.mock(GeoZoneRepository.class);
    private final UserRepository userRepo = Mockito.mock(UserRepository.class);
    private final NotificationRepository notificationRepo = Mockito.mock(NotificationRepository.class);
    private final EmergencyTelegramBot bot = Mockito.mock(EmergencyTelegramBot.class);
    private final GeoService geoService = new GeoService();

    private final EmergencyEventService service =
            new EmergencyEventService(eventRepo, geoRepo, userRepo, notificationRepo, bot, geoService);

    @Test
    void shouldCreateEventObject() {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test");
        request.setMessageText("Message");
        request.setPriority("HIGH");
        request.setCity("Berlin");
        request.setCenterLat(52.52);
        request.setCenterLng(13.405);
        request.setRadiusKm(5.0);

        Mockito.when(eventRepo.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        Mockito.when(geoRepo.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        Mockito.when(userRepo.findAll())
                .thenReturn(Collections.emptyList());

        EmergencyEvent result = service.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}