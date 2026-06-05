package com.emergency.alert.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private Double latitude;
    private Double longitude;
}