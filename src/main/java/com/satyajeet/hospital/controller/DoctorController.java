package com.satyajeet.hospital.controller;
import com.satyajeet.hospital.dto.DoctorRequest;
import com.satyajeet.hospital.entity.Doctor;
import com.satyajeet.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/doctors") @RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<Page<Doctor>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.getAllDoctors(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody DoctorRequest req) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, req));
    }

    @GetMapping("/department/{dept}")
    public ResponseEntity<List<Doctor>> getDoctorsByDepartment(@PathVariable String dept) {
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(dept));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Doctor> updateStatus(@PathVariable Long id, @RequestParam Doctor.Status status) {
        return ResponseEntity.ok(doctorService.updateStatus(id, status));
    }
}
