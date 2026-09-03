package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.response.TreatmentTypeResponseDTO;
import com.sunrise.dentalclinic.repository.TreatmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//only treatment types need for selecting appointments and billing
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentTypeController {

    private final TreatmentTypeRepository treatmentTypeRepository;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            var treatments = treatmentTypeRepository.findAll().stream()
                    .map(t -> new TreatmentTypeResponseDTO(t.getId(), t.getName(), t.getFee()))
                    .toList();
            return ResponseEntity.ok(treatments);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
