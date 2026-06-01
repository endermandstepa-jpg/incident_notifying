package com.emergency.alert.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "geo_zones")
public class GeoZone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String eventId;
    private String city;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}