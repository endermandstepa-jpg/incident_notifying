package com.emergency.alert.controller;

import com.emergency.alert.entity.User;
import com.emergency.alert.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping
    public List<User> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public User byId(
            @PathVariable UUID id
    ) {
        return repository.findById(id)
                .orElseThrow();
    }

    @PutMapping("/{id}")
    public User update(
            @PathVariable UUID id,
            @RequestBody User request
    ) {

        User user =
                repository.findById(id)
                        .orElseThrow();

        user.setFullName(
                request.getFullName());

        user.setCity(
                request.getCity());

        user.setLatitude(
                request.getLatitude());

        user.setLongitude(
                request.getLongitude());

        return repository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ) {
        repository.deleteById(id);
    }
}