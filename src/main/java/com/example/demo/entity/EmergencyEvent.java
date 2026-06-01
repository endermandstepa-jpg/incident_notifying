package com.emergency.alert.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emergency_events")
public class EmergencyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;
    
    private String title;
    private String messageText;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        if (eventId == null) {
            eventId = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }
}