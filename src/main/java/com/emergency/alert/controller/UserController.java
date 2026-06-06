package com.emergency.alert.controller;

import com.emergency.alert.dto.UpdateUserRequest;
import com.emergency.alert.entity.User;
import com.emergency.alert.storage.InMemoryDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    public List<User> all() {
        return new ArrayList<>(InMemoryDatabase.USERS.values());
    }

    @GetMapping("/{id}")
    public User byId(@PathVariable Long id) {
        return InMemoryDatabase.USERS.get(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {

        try {
            User user = InMemoryDatabase.USERS.get(id);

            if (user == null) return null;

            user.setFullName(request.getFullName());
            user.setLatitude(request.getLatitude());
            user.setLongitude(request.getLongitude());

            InMemoryDatabase.USERS.put(id, user);

            return user;

        } catch (Exception e) {
            log.error("UPDATE USER FAILED", e);
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        InMemoryDatabase.USERS.remove(id);
        return "OK";
    }
}