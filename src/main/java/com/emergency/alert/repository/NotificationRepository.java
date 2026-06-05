package com.emergency.alert.repository;

import com.emergency.alert.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    Notification findTopByUserIdOrderBySentAtDesc(Long userId);
}