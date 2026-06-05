package com.emergency.alert.dto;

import lombok.Data;

@Data
public class EventResponseStatusDto {
    private Long userId;
    private String userName;
    private String responseType;
}