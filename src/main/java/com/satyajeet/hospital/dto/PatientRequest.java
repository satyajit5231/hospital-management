package com.satyajeet.hospital.dto;
import com.satyajeet.hospital.entity.Patient;
import lombok.Data;
import java.time.LocalDate;
@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String medicalHistory;
}
