package com.emergency.alert.controller;

import com.emergency.alert.entity.User;
import com.emergency.alert.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserRepository repo = Mockito.mock(UserRepository.class);

    private final UserController controller = new UserController(repo);

    @Test
    void shouldReturnUsers() {

        Mockito.when(repo.findAll()).thenReturn(List.of(new User()));

        var result = controller.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}