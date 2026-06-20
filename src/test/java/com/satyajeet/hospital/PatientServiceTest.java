package com.satyajeet.hospital;
import com.satyajeet.hospital.dto.PatientRequest;
import com.satyajeet.hospital.entity.Patient;
import com.satyajeet.hospital.exception.*;
import com.satyajeet.hospital.repository.PatientRepository;
import com.satyajeet.hospital.service.PatientService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock PatientRepository patientRepository;
    @InjectMocks PatientService patientService;

    @Test void shouldCreatePatient() {
        PatientRequest req = new PatientRequest();
        req.setFirstName("Ravi"); req.setLastName("Kumar"); req.setEmail("ravi@test.com");
        when(patientRepository.existsByEmail("ravi@test.com")).thenReturn(false);
        when(patientRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Patient result = patientService.createPatient(req);
        assertThat(result.getFirstName()).isEqualTo("Ravi");
        verify(patientRepository).save(any(Patient.class));
    }

    @Test void shouldThrowOnDuplicateEmail() {
        PatientRequest req = new PatientRequest();
        req.setEmail("dup@test.com");
        when(patientRepository.existsByEmail("dup@test.com")).thenReturn(true);
        assertThatThrownBy(() -> patientService.createPatient(req))
                .isInstanceOf(ConflictException.class);
    }

    @Test void shouldGetAllPatients() {
        Page<Patient> page = new PageImpl<>(List.of(new Patient()));
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(page);
        assertThat(patientService.getAllPatients(0, 10)).hasSize(1);
    }

    @Test void shouldThrowWhenPatientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> patientService.getPatientById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test void shouldDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        patientService.deletePatient(1L);
        verify(patientRepository).deleteById(1L);
    }
}
