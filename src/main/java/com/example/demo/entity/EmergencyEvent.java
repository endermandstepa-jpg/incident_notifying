package com.emergency.alert.entity;

import javax.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "emergency_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String messageText;

    private String priority;

    private String status;

    private Instant createdAt;

    private UUID createdBy;
}