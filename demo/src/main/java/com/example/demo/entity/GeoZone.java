package com.emergency.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "geo_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoZone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID zoneId;

    private UUID eventId;

    private String city;

    private Double centerLat;

    private Double centerLng;

    private Double radiusKm;
}