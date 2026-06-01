package com.emergency.alert.repository;

import com.emergency.alert.entity.GeoZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GeoZoneRepository extends JpaRepository<GeoZone, UUID> {

    Optional<GeoZone> findByEventId(UUID eventId);
}