package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.response.HelpSectionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// step by step instructions for new users
@RestController
@RequestMapping("/api/help")
public class HelpController {

    @GetMapping
    public ResponseEntity<List<HelpSectionResponseDTO>> getHelp() {
        List<HelpSectionResponseDTO> sections = List.of(
                new HelpSectionResponseDTO("Login", List.of(
                        "Go to the login page.",
                        "Enter your username and password.",
                        "Click Login."
                )),
                new HelpSectionResponseDTO("Register a New Appointment", List.of(
                        "Go to the appointment registration page.",
                        "Enter the patient name, address, and contact number.",
                        "Select the dentist and treatment type.",
                        "Select the appointment date and time.",
                        "Click Register to save the appointment.",
                        "Note down the appointment number shown."
                )),
                new HelpSectionResponseDTO("Search an Appointment", List.of(
                        "Go to the search page.",
                        "Enter the appointment number.",
                        "Click Search to view the appointment details."
                )),
                new HelpSectionResponseDTO("Generate a Bill", List.of(
                        "Go to the billing page.",
                        "Enter the appointment number.",
                        "Click Generate Bill.",
                        "The total amount is calculated and shown automatically."
                )),
                new HelpSectionResponseDTO("Manage Patients", List.of(
                        "Go to the patients page.",
                        "Add, view, update, or delete patient records as needed."
                ))
        );

        return ResponseEntity.ok(sections);
    }
}
