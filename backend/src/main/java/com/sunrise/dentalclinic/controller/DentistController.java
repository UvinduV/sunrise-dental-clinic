package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.response.DentistResponseDTO;
import com.sunrise.dentalclinic.repository.DentistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//only dentists need for selecting appointments and billing, so no need for a separate service for dentists
@RestController
@RequestMapping("/api/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistRepository dentistRepository;

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            var dentists = dentistRepository.findAll().stream()
                    .map(d -> new DentistResponseDTO(d.getId(), d.getName(), d.getSpecialization(), d.getConsultationFee()))
                    .toList();
            return ResponseEntity.ok(dentists);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
