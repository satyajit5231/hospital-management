package com.satyajeet.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "patients")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String firstName;
    @Column(nullable = false) private String lastName;
    @Column(unique = true, nullable = false) private String email;
    private String phone;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING) private Gender gender;
    private String address;
    private String bloodGroup;
    @Column(name = "emergency_contact") private String emergencyContact;
    @Column(name = "medical_history", columnDefinition = "TEXT") private String medicalHistory;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
    public enum Gender { MALE, FEMALE, OTHER }
}
