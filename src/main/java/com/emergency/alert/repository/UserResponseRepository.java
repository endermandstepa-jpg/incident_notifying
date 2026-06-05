package com.emergency.alert.repository;

import com.emergency.alert.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {

    List<UserResponse> findByNotificationIdIn(List<Long> notificationIds);
}