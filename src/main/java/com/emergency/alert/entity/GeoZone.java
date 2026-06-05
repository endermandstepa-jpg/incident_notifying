package com.emergency.alert.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private String city;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}