package com.emergency.alert.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventInfoResponse {
    private Long eventId;
    private LocalDateTime createdAt;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}