package com.emergency.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String messengerId;

    private String city;

    private Double latitude;

    private Double longitude;

    private Instant createdAt;
}