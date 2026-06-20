package com.satyajeet.hospital.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "departments")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) private String name;
    private String description;
    @Column(name = "head_doctor") private String headDoctor;
    @Column(name = "total_beds") private Integer totalBeds;
    @Column(name = "available_beds") private Integer availableBeds;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
