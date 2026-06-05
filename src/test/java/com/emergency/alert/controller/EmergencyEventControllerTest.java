package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.service.EmergencyEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyEventControllerTest {

    private final EmergencyEventService service = Mockito.mock(EmergencyEventService.class);

    private final EmergencyEventController controller =
            new EmergencyEventController(
                    service,
                    Mockito.mock(com.emergency.alert.repository.EmergencyEventRepository.class),
                    Mockito.mock(com.emergency.alert.repository.GeoZoneRepository.class),
                    Mockito.mock(com.emergency.alert.repository.NotificationRepository.class),
                    Mockito.mock(com.emergency.alert.repository.UserResponseRepository.class),
                    Mockito.mock(com.emergency.alert.repository.UserRepository.class)
            );

    @Test
    void shouldCallServiceAndReturnEvent() {

        CreateEventRequest req = new CreateEventRequest();
        req.setTitle("Test");

        EmergencyEvent event = new EmergencyEvent();
        event.setTitle("Test");

        Mockito.when(service.create(Mockito.any())).thenReturn(event);

        EmergencyEvent result = controller.create(req);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}