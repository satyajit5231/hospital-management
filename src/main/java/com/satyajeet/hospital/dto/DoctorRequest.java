package com.satyajeet.hospital.dto;
import lombok.Data;
@Data
public class DoctorRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String department;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private Double consultationFee;
}
