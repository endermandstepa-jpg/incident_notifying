package com.emergency.alert.controller;

import com.emergency.alert.config.SecurityConfig;
import com.emergency.alert.dto.CreateEventRequest;
import com.emergency.alert.entity.EmergencyEvent;
import com.emergency.alert.repository.EmergencyEventRepository;
import com.emergency.alert.service.EmergencyEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // выключаем security
class EmergencyEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyEventService service;

    @MockBean // 🔥 ВАЖНО — теперь Spring не падает
    private EmergencyEventRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEvent() throws Exception {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Fire");
        request.setMessageText("Evacuate");
        request.setCity("Berlin");

        when(service.create(any(CreateEventRequest.class)))
                .thenReturn(new EmergencyEvent());

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}