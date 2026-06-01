package com.emergency.alert.repository;

import com.emergency.alert.entity.EmergencyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent, Long> {
    Optional<EmergencyEvent> findTopByOrderByCreatedAtDesc();
}