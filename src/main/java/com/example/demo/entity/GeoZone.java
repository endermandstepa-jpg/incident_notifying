package com.emergency.alert.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geo_zones")
public class GeoZone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "event_id")
    private UUID eventId;
    
    private String city;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}