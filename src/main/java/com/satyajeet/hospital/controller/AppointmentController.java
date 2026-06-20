package com.satyajeet.hospital.controller;
import com.satyajeet.hospital.dto.AppointmentRequest;
import com.satyajeet.hospital.entity.Appointment;
import com.satyajeet.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/appointments") @RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<Page<Appointment>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createAppointment(req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id, @RequestParam Appointment.Status status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<Appointment>> getByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId, page, size));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<Appointment>> getByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId, page, size));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
