package com.satyajeet.hospital.service;
import com.satyajeet.hospital.dto.AppointmentRequest;
import com.satyajeet.hospital.entity.Appointment;
import com.satyajeet.hospital.exception.*;
import com.satyajeet.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public Page<Appointment> getAllAppointments(int page, int size) {
        return appointmentRepository.findAll(PageRequest.of(page, size, Sort.by("appointmentDate").descending()));
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
    }

    @Transactional
    public Appointment createAppointment(AppointmentRequest req) {
        var patient = patientRepository.findById(req.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + req.getPatientId()));
        var doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + req.getDoctorId()));

        // Conflict check — 30-minute slots
        LocalDateTime start = req.getAppointmentDate().minusMinutes(29);
        LocalDateTime end = req.getAppointmentDate().plusMinutes(29);
        List<Appointment> conflicts = appointmentRepository
                .findConflictingAppointments(req.getDoctorId(), start, end);
        if (!conflicts.isEmpty())
            throw new ConflictException("Doctor already has an appointment in this time slot");

        Appointment appointment = Appointment.builder()
                .patient(patient).doctor(doctor)
                .appointmentDate(req.getAppointmentDate())
                .reason(req.getReason()).notes(req.getNotes())
                .department(req.getDepartment())
                .build();
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateStatus(Long id, Appointment.Status status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public Page<Appointment> getAppointmentsByPatient(Long patientId, int page, int size) {
        return appointmentRepository.findByPatientId(patientId, PageRequest.of(page, size));
    }

    public Page<Appointment> getAppointmentsByDoctor(Long doctorId, int page, int size) {
        return appointmentRepository.findByDoctorId(doctorId, PageRequest.of(page, size));
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appointment);
    }
}
