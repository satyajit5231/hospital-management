package com.satyajeet.hospital.service;
import com.satyajeet.hospital.dto.PatientRequest;
import com.satyajeet.hospital.entity.Patient;
import com.satyajeet.hospital.exception.*;
import com.satyajeet.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Page<Patient> getAllPatients(int page, int size) {
        return patientRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    @Transactional
    public Patient createPatient(PatientRequest req) {
        if (patientRepository.existsByEmail(req.getEmail()))
            throw new ConflictException("Patient with email already exists");
        Patient patient = Patient.builder()
                .firstName(req.getFirstName()).lastName(req.getLastName())
                .email(req.getEmail()).phone(req.getPhone())
                .dateOfBirth(req.getDateOfBirth()).gender(req.getGender())
                .address(req.getAddress()).bloodGroup(req.getBloodGroup())
                .emergencyContact(req.getEmergencyContact()).medicalHistory(req.getMedicalHistory())
                .build();
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, PatientRequest req) {
        Patient patient = getPatientById(id);
        patient.setFirstName(req.getFirstName()); patient.setLastName(req.getLastName());
        patient.setPhone(req.getPhone()); patient.setAddress(req.getAddress());
        patient.setBloodGroup(req.getBloodGroup()); patient.setMedicalHistory(req.getMedicalHistory());
        patient.setEmergencyContact(req.getEmergencyContact());
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id))
            throw new ResourceNotFoundException("Patient not found: " + id);
        patientRepository.deleteById(id);
    }

    public Page<Patient> searchPatients(String name, int page, int size) {
        return patientRepository.searchByName(name, PageRequest.of(page, size));
    }
}
