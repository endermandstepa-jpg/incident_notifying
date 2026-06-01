package com.emergency.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID responseId;

    private UUID notificationId;

    private UUID userId;

    private String responseType;

    private Instant responseTime;

    @Column(columnDefinition = "TEXT")
    private String comment;
}