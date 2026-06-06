package com.emergency.alert.service;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.storage.InMemoryDatabase;
import com.emergency.alert.telegram.EmergencyTelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyEventServiceTest {

    private final EmergencyTelegramBot bot = new EmergencyTelegramBot();
    private final GeoService geoService = new GeoService();

    private final EmergencyEventService service =
            new EmergencyEventService(bot, geoService);

    @BeforeEach
    void setUp() {
        InMemoryDatabase.EVENTS.clear();
        InMemoryDatabase.USERS.clear();
        InMemoryDatabase.NOTIFICATIONS.clear();
        InMemoryDatabase.RESPONSES.clear();
    }

    @Test
    void shouldCreateEvent() {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test");
        request.setMessageText("Message");
        request.setCity("Berlin");

        EmergencyEvent result = service.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}