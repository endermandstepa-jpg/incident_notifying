package com.emergency.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseStatusDto {

    private Long userId;
    private String userName;
    private String responseType;
}