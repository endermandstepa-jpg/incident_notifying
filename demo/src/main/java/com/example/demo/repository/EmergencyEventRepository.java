package com.emergency.alert.repository;

import com.emergency.alert.entity.EmergencyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmergencyEventRepository
        extends JpaRepository<EmergencyEvent, UUID> {

    Optional<EmergencyEvent> findTopByOrderByCreatedAtDesc();
}