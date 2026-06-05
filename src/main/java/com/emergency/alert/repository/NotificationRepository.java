package com.emergency.alert.repository;

import com.emergency.alert.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    Optional<Notification> findTopByUserIdOrderBySentAtDesc(Long userId);

    List<Notification> findByEventId(Long eventId);
}