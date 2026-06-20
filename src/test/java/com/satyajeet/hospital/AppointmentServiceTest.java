package com.satyajeet.hospital;
import com.satyajeet.hospital.dto.AppointmentRequest;
import com.satyajeet.hospital.entity.*;
import com.satyajeet.hospital.exception.*;
import com.satyajeet.hospital.repository.*;
import com.satyajeet.hospital.service.AppointmentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock AppointmentRepository appointmentRepository;
    @Mock PatientRepository patientRepository;
    @Mock DoctorRepository doctorRepository;
    @InjectMocks AppointmentService appointmentService;

    @Test void shouldCreateAppointmentSuccessfully() {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId(1L); req.setDoctorId(1L);
        req.setAppointmentDate(LocalDateTime.now().plusDays(1));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(new Doctor()));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any())).thenReturn(List.of());
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Appointment result = appointmentService.createAppointment(req);
        assertThat(result).isNotNull();
    }

    @Test void shouldThrowOnConflictingAppointment() {
        AppointmentRequest req = new AppointmentRequest();
        req.setPatientId(1L); req.setDoctorId(1L);
        req.setAppointmentDate(LocalDateTime.now().plusDays(1));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(new Patient()));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(new Doctor()));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any()))
                .thenReturn(List.of(new Appointment()));
        assertThatThrownBy(() -> appointmentService.createAppointment(req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("time slot");
    }

    @Test void shouldCancelAppointment() {
        Appointment appt = new Appointment();
        appt.setId(1L); appt.setStatus(Appointment.Status.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        appointmentService.cancelAppointment(1L);
        assertThat(appt.getStatus()).isEqualTo(Appointment.Status.CANCELLED);
    }

    @Test void shouldThrowWhenAppointmentNotFound() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> appointmentService.getAppointmentById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
