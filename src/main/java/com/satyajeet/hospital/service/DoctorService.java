package com.satyajeet.hospital.service;
import com.satyajeet.hospital.dto.DoctorRequest;
import com.satyajeet.hospital.entity.Doctor;
import com.satyajeet.hospital.exception.*;
import com.satyajeet.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public Page<Doctor> getAllDoctors(int page, int size) {
        return doctorRepository.findAll(PageRequest.of(page, size, Sort.by("lastName")));
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    @Transactional
    public Doctor createDoctor(DoctorRequest req) {
        if (doctorRepository.existsByEmail(req.getEmail()))
            throw new ConflictException("Doctor with email already exists");
        Doctor doctor = Doctor.builder()
                .firstName(req.getFirstName()).lastName(req.getLastName())
                .email(req.getEmail()).phone(req.getPhone())
                .specialization(req.getSpecialization()).department(req.getDepartment())
                .licenseNumber(req.getLicenseNumber()).yearsOfExperience(req.getYearsOfExperience())
                .consultationFee(req.getConsultationFee())
                .build();
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor updateDoctor(Long id, DoctorRequest req) {
        Doctor doctor = getDoctorById(id);
        doctor.setFirstName(req.getFirstName()); doctor.setLastName(req.getLastName());
        doctor.setPhone(req.getPhone()); doctor.setSpecialization(req.getSpecialization());
        doctor.setDepartment(req.getDepartment()); doctor.setConsultationFee(req.getConsultationFee());
        doctor.setYearsOfExperience(req.getYearsOfExperience());
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department);
    }

    @Transactional
    public Doctor updateStatus(Long id, Doctor.Status status) {
        Doctor doctor = getDoctorById(id);
        doctor.setStatus(status);
        return doctorRepository.save(doctor);
    }
}
