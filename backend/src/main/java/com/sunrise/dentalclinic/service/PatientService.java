package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.PatientRequestDTO;
import com.sunrise.dentalclinic.dto.response.PatientResponseDTO;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.exception.PatientHasAppointmentsException;
import com.sunrise.dentalclinic.exception.PatientNotFoundException;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientResponseDTO create(PatientRequestDTO request) {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAddress(request.getAddress());
        patient.setContactNumber(request.getContactNumber());
        patient = patientRepository.save(patient);

        return toResponse(patient);
    }

    public PatientResponseDTO findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with id: " + id));

        return toResponse(patient);
    }

    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PatientResponseDTO update(Long id, PatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with id: " + id));

        patient.setName(request.getName());
        patient.setAddress(request.getAddress());
        patient.setContactNumber(request.getContactNumber());
        patient = patientRepository.save(patient);

        return toResponse(patient);
    }

    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("No patient found with id: " + id);
        }
        if (appointmentRepository.existsByPatientId(id)) {
            throw new PatientHasAppointmentsException(
                    "This patient has appointment(s) and cannot be deleted.");
        }
        patientRepository.deleteById(id);
    }

    public boolean hasAppointments(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("No patient found with id: " + id);
        }
        return appointmentRepository.existsByPatientId(id);
    }

    private PatientResponseDTO toResponse(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getAddress(),
                patient.getContactNumber()
        );
    }
}
