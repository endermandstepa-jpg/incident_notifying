package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.service.EmergencyEventService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyEventControllerTest {

    private final EmergencyEventService service = mock(EmergencyEventService.class);
    private final EmergencyEventController controller = new EmergencyEventController(service);

    @Test
    void shouldCreateEvent() {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Fire");
        request.setMessageText("Evacuate");
        request.setCity("Berlin");

        EmergencyEvent event = new EmergencyEvent();
        event.setTitle("Fire");

        when(service.create(any(CreateEventRequest.class)))
                .thenReturn(event);

        EmergencyEvent result = controller.createEvent(request);

        assertNotNull(result);
        assertEquals("Fire", result.getTitle());
        verify(service, times(1)).create(request);
    }
}