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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    
    @Column(unique = true)
    private String messengerId;
    
    private String fullName;
    private String city;
    private Double latitude;
    private Double longitude;
    private LocalDateTime registeredAt;
    
    @PrePersist
    public void prePersist() {
        if (userId == null) {
            userId = UUID.randomUUID();
        }
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }
}