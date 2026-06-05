package com.emergency.alert.controller;

import com.emergency.alert.service.EmergencyEventService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyEventControllerTest {

    private final EmergencyEventService service = mock(EmergencyEventService.class);
    private final EmergencyEventController controller = new EmergencyEventController(
            service,
            mock(com.emergency.alert.repository.EmergencyEventRepository.class),
            mock(com.emergency.alert.repository.GeoZoneRepository.class),
            mock(com.emergency.alert.repository.NotificationRepository.class),
            mock(com.emergency.alert.repository.UserResponseRepository.class),
            mock(com.emergency.alert.repository.UserRepository.class)
    );

    @Test
    void shouldCreateEvent() {

        var request = new com.emergency.alert.dto.CreateEventRequest();
        request.setTitle("Test");

        when(service.create(any())).thenReturn(new com.emergency.alert.entity.EmergencyEvent());

        var result = controller.createEvent(request);

        assertNotNull(result);
    }
}