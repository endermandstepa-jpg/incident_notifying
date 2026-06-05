package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.repository.*;
import com.emergency.alert.service.EmergencyEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyEventControllerTest {

    private final EmergencyEventService service = Mockito.mock(EmergencyEventService.class);

    private final EmergencyEventController controller =
            new EmergencyEventController(
                    service,
                    Mockito.mock(EmergencyEventRepository.class),
                    Mockito.mock(GeoZoneRepository.class),
                    Mockito.mock(NotificationRepository.class),
                    Mockito.mock(UserResponseRepository.class),
                    Mockito.mock(UserRepository.class)
            );

    @Test
    void shouldCreateEvent() {

        CreateEventRequest request = new CreateEventRequest();

        EmergencyEvent event = new EmergencyEvent();
        event.setTitle("Test");

        Mockito.when(service.create(Mockito.any())).thenReturn(event);

        EmergencyEvent result = controller.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}