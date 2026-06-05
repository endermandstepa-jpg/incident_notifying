package com.emergency.alert.controller;

import com.emergency.alert.entity.User;
import com.emergency.alert.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserController controller = new UserController(repository);

    @Test
    void shouldReturnUsers() {

        when(repository.findAll()).thenReturn(List.of(new User()));

        List<User> result = controller.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}