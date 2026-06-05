package com.emergency.alert.controller;

import com.emergency.alert.config.SecurityConfig;
import com.emergency.alert.entity.User;
import com.emergency.alert.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    // важно: контроллер не зависит от filter напрямую, но Spring Security требует bean graph
    @MockBean
    private com.emergency.alert.config.ApiKeyFilter apiKeyFilter;

    @Test
    void shouldReturnUsers() throws Exception {

        when(repository.findAll()).thenReturn(List.of(new User()));

        mockMvc.perform(get("/api/users")
                        .header("X-API-Key", "default-key-for-development"))
                .andExpect(status().isOk());
    }
}