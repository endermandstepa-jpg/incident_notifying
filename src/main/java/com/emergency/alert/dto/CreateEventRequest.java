package com.emergency.alert.dto;

import lombok.Data;

@Data
public class CreateEventRequest {
    private String title;
    private String messageText;
    private String priority;

    private String city;
    private Double centerLat;
    private Double centerLng;
    private Double radiusKm;
}