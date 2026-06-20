package com.satyajeet.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "doctors")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String firstName;
    @Column(nullable = false) private String lastName;
    @Column(unique = true, nullable = false) private String email;
    private String phone;
    private String specialization;
    private String department;
    private String licenseNumber;
    @Column(name = "years_of_experience") private Integer yearsOfExperience;
    @Column(name = "consultation_fee") private Double consultationFee;
    @Enumerated(EnumType.STRING) private Status status;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); if (status == null) status = Status.ACTIVE; }
    public enum Status { ACTIVE, INACTIVE, ON_LEAVE }
}
