package com.emergency.alert.controller;

import com.emergency.alert.config.SecurityConfig;
import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.service.EmergencyEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmergencyEventController.class)
@Import(SecurityConfig.class)
class EmergencyEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyEventService service;

    @MockBean
    private com.emergency.alert.repository.EmergencyEventRepository emergencyEventRepository;

    @MockBean
    private com.emergency.alert.repository.GeoZoneRepository geoZoneRepository;

    @MockBean
    private com.emergency.alert.repository.NotificationRepository notificationRepository;

    @MockBean
    private com.emergency.alert.repository.UserResponseRepository userResponseRepository;

    @MockBean
    private com.emergency.alert.repository.UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEvent() throws Exception {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Fire");
        request.setMessageText("Evacuate");
        request.setCity("Berlin");

        when(service.create(any())).thenReturn(new EmergencyEvent());

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}