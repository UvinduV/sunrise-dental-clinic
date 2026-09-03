package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.dto.request.AppointmentRequestDTO;
import com.sunrise.dentalclinic.exception.AppointmentNotFoundException;
import com.sunrise.dentalclinic.exception.DentistNotFoundException;
import com.sunrise.dentalclinic.exception.DoubleBookingException;
import com.sunrise.dentalclinic.exception.TreatmentTypeNotFoundException;
import com.sunrise.dentalclinic.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    //Appointment Registration
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody AppointmentRequestDTO request) {
        try {
            return new ResponseEntity<>(appointmentService.register(request), HttpStatus.CREATED);
        } catch (DentistNotFoundException | TreatmentTypeNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DoubleBookingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Search Appointment by Appointment Number
    @GetMapping("/{appointmentNo}")
    public ResponseEntity<?> search(@PathVariable String appointmentNo) {
        try {
            return ResponseEntity.ok(appointmentService.findByAppointmentNo(appointmentNo));
        } catch (AppointmentNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //List all appointments
    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok(appointmentService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
