package com.satyajeet.hospital.repository;
import com.satyajeet.hospital.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    Page<Appointment> findByStatus(Appointment.Status status, Pageable pageable);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate BETWEEN :start AND :end AND a.status != 'CANCELLED'")
    List<Appointment> findConflictingAppointments(Long doctorId, LocalDateTime start, LocalDateTime end);
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate >= :start AND a.appointmentDate < :end")
    Long countByDateRange(LocalDateTime start, LocalDateTime end);
}
