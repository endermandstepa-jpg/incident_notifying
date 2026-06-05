package com.emergency.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventInfoResponse {

    private Long eventId;
    private String title;
    private String messageText;

    private LocalDateTime createdAt;

    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}