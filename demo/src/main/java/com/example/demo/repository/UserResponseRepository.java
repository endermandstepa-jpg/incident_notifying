package com.emergency.alert.repository;

import com.emergency.alert.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserResponseRepository
        extends JpaRepository<UserResponse, UUID> {
}