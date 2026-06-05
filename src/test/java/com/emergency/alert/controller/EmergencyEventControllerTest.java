package com.emergency.alert.controller;

import com.emergency.alert.dto.CreateEventRequest;
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
@AutoConfigureMockMvc(addFilters = false) // 💥 убирает 401 полностью
class EmergencyEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmergencyEventService service;

    @Test
    void shouldCreateEvent() throws Exception {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test");
        request.setMessageText("Message");
        request.setCity("Berlin");

        when(service.create(any())).thenReturn(null);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}