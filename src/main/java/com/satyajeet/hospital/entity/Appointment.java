package com.satyajeet.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "appointments")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column(name = "appointment_date", nullable = false) private LocalDateTime appointmentDate;
    @Enumerated(EnumType.STRING) private Status status;
    private String reason;
    @Column(columnDefinition = "TEXT") private String notes;
    private String department;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() {
        createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now();
        if (status == null) status = Status.SCHEDULED;
    }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
    public enum Status { SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW }
}
