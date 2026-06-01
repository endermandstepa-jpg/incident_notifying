package com.emergency.alert.entity;

import javax.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private UUID eventId;

    private UUID userId;

    private Instant sentAt;

    private String deliveryStatus;
}