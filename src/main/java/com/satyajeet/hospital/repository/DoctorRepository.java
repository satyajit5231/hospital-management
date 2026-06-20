package com.satyajeet.hospital.repository;
import com.satyajeet.hospital.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Doctor> findByDepartment(String department);
    Page<Doctor> findBySpecialization(String specialization, Pageable pageable);
    List<Doctor> findByStatus(Doctor.Status status);
}
