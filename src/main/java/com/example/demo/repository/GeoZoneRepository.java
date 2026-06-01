package com.emergency.alert.repository;

import com.emergency.alert.entity.GeoZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GeoZoneRepository extends JpaRepository<GeoZone, Long> {
    Optional<GeoZone> findByEventId(Long eventId);
}