package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.AppointmentRequestDTO;
import com.sunrise.dentalclinic.dto.response.AppointmentResponseDTO;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.TreatmentType;
import com.sunrise.dentalclinic.exception.AppointmentNotFoundException;
import com.sunrise.dentalclinic.exception.DentistNotFoundException;
import com.sunrise.dentalclinic.exception.DoubleBookingException;
import com.sunrise.dentalclinic.exception.TreatmentTypeNotFoundException;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.DentistRepository;
import com.sunrise.dentalclinic.repository.PatientRepository;
import com.sunrise.dentalclinic.repository.TreatmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;

    @Transactional
    public AppointmentResponseDTO register(AppointmentRequestDTO request) {
        Dentist dentist = dentistRepository.findById(request.getDentistId())
                .orElseThrow(() -> new DentistNotFoundException(
                        "No dentist found with id: " + request.getDentistId()));

        TreatmentType treatment = treatmentTypeRepository.findById(request.getTreatmentId())
                .orElseThrow(() -> new TreatmentTypeNotFoundException(
                        "No treatment type found with id: " + request.getTreatmentId()));

        boolean alreadyBooked = appointmentRepository.existsByDentistIdAndDateAndTimeAndStatus(
                dentist.getId(), request.getDate(), request.getTime(), Appointment.AppointmentStatus.SCHEDULED);
        if (alreadyBooked) {
            throw new DoubleBookingException(
                    "Dentist " + dentist.getName() + " already has a scheduled appointment at "
                            + request.getDate() + " " + request.getTime());
        }

        Patient patient = findOrRegisterPatient(request);

        Appointment appointment = new Appointment();
        appointment.setAppointmentNo(generateAppointmentNo());
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setTreatment(treatment);
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment = appointmentRepository.save(appointment);

        return toResponse(appointment);
    }

    public AppointmentResponseDTO findByAppointmentNo(String appointmentNo) {
        Appointment appointment = appointmentRepository.findByAppointmentNo(appointmentNo)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "No appointment found with appointment number: " + appointmentNo));

        return toResponse(appointment);
    }

    public List<AppointmentResponseDTO> findAll() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // search returning patient
    private Patient findOrRegisterPatient(AppointmentRequestDTO request) {
        return patientRepository.findByContactNumber(request.getContactNumber())
                .orElseGet(() -> {
                    Patient patient = new Patient();
                    patient.setName(request.getPatientName());
                    patient.setAddress(request.getAddress());
                    patient.setContactNumber(request.getContactNumber());
                    return patientRepository.save(patient);
                });
    }

    private String generateAppointmentNo() {
        long nextSeq = appointmentRepository.count() + 1;
        return String.format("APT-%05d", nextSeq);
    }

    private AppointmentResponseDTO toResponse(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getAppointmentNo(),
                appointment.getPatient().getName(),
                appointment.getPatient().getAddress(),
                appointment.getPatient().getContactNumber(),
                appointment.getDentist().getName(),
                appointment.getTreatment().getName(),
                appointment.getTreatment().getFee(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getStatus().name()
        );
    }
}
