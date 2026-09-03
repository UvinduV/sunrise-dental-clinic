package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.PatientRequest;
import com.sunrise.dentalclinic.dto.response.PatientResponse;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.exception.PatientNotFoundException;
import com.sunrise.dentalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAddress(request.getAddress());
        patient.setContactNumber(request.getContactNumber());
        patient = patientRepository.save(patient);

        return toResponse(patient);
    }

    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No patient found with id: " + id));

        return toResponse(patient);
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
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
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getName(),
                patient.getAddress(),
                patient.getContactNumber()
        );
    }
}
