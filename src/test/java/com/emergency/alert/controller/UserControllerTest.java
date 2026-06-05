package com.emergency.alert.controller;

import com.emergency.alert.entity.User;
import com.emergency.alert.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserRepository repository = Mockito.mock(UserRepository.class);

    private final UserController controller = new UserController(repository);

    @Test
    void shouldReturnAllUsers() {

        Mockito.when(repository.findAll())
                .thenReturn(List.of(new User()));

        List<User> result = controller.all();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}