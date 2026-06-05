package com.emergency.alert.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventInfoResponse {
    private Long eventId;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
    private LocalDateTime createdAt;
}