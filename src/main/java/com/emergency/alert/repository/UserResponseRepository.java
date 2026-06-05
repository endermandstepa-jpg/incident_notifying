package com.emergency.alert.repository;

import com.emergency.alert.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {
}

List<UserResponse> findByNotificationId(Long notificationId);