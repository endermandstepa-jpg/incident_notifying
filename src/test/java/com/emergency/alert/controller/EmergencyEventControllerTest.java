package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.service.EmergencyEventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class EmergencyEventControllerTest {

    private final EmergencyEventService service =
            Mockito.mock(EmergencyEventService.class);

    private final EmergencyEventController controller =
            new EmergencyEventController(service);

    @Test
    void shouldCreateEvent() {

        CreateEventRequest request = new CreateEventRequest();

        request.setTitle("Test");
        request.setMessageText("Message");

        EmergencyEvent mockEvent = EmergencyEvent.builder()
                .id(1L)
                .title("Test")
                .messageText("Message")
                .build();

        Mockito.when(service.create(Mockito.any()))
                .thenReturn(mockEvent);

        EmergencyEvent result = controller.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}