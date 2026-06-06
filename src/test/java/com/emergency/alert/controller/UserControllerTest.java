package com.emergency.alert.controller;

import com.emergency.alert.entity.User;
import com.emergency.alert.storage.InMemoryDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController controller = new UserController();

    @BeforeEach
    void setUp() {
        InMemoryDatabase.USERS.clear();

        User user = User.builder()
                .id(1L)
                .fullName("Test")
                .messengerId("123")
                .build();

        InMemoryDatabase.USERS.put(1L, user);
    }

    @Test
    void shouldReturnAllUsers() {

        List<User> result = controller.all();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}