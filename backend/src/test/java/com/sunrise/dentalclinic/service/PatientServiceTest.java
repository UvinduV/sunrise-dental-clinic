package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.PatientRequestDTO;
import com.sunrise.dentalclinic.dto.response.PatientResponseDTO;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.exception.PatientNotFoundException;
import com.sunrise.dentalclinic.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private PatientRequestDTO validRequest() {
        return new PatientRequestDTO("Kamal", "123 Main St, Colombo", "0771234566");
    }

    @Test
    void create_validRequest_saved() {
        Patient saved = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        PatientResponseDTO response = patientService.create(validRequest());

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Kamal");
    }

    @Test
    void findById_existingId_found() {
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientResponseDTO response = patientService.findById(1L);

        assertThat(response.getName()).isEqualTo("Kamal");
    }

    @Test
    void findById_unknownId_rejected() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(99L))
                .isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    void findAll_hasPatients_returnsList() {
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<PatientResponseDTO> responses = patientService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("Kamal");
    }

    @Test
    void update_existingId_updated() {
        Patient existing = new Patient(1L, "Kamal", "Old Address", "0771234566");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PatientResponseDTO response = patientService.update(1L,
                new PatientRequestDTO("Kamal Updated", "New Address", "0771234567"));

        assertThat(response.getName()).isEqualTo("Kamal Updated");
        assertThat(response.getAddress()).isEqualTo("New Address");
    }

    @Test
    void update_unknownId_rejected() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.update(99L, validRequest()))
                .isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    void delete_existingId_deleted() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.delete(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void delete_unknownId_rejected() {
        when(patientRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> patientService.delete(99L))
                .isInstanceOf(PatientNotFoundException.class);
    }
}
