package com.satyajeet.hospital.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentDate;
    private String reason;
    private String notes;
    private String department;
}
