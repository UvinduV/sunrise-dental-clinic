package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.request.AppointmentRequest;
import com.sunrise.dentalclinic.dto.response.AppointmentResponse;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DentistRepository dentistRepository;

    @Mock
    private TreatmentTypeRepository treatmentTypeRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentRequest validRequest() {
        return new AppointmentRequest(
                "Kamal",
                "123 Main St, Colombo",
                "0771234566",
                1L,
                1L,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30)
        );
    }

    @Test
    void register_validRequest_saved() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient savedPatient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");

        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(treatmentTypeRepository.findById(1L)).thenReturn(Optional.of(treatment));
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);
        when(appointmentRepository.count()).thenReturn(0L);
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentResponse response = appointmentService.register(validRequest());

        assertThat(response.getAppointmentNo()).isEqualTo("APT-00001");
        assertThat(response.getPatientName()).isEqualTo("Kamal");
        assertThat(response.getDentistName()).isEqualTo("Dr. Silva");
        assertThat(response.getTreatmentName()).isEqualTo("Root Canal");
        assertThat(response.getFee()).isEqualByComparingTo("5000");
        assertThat(response.getStatus()).isEqualTo("SCHEDULED");
    }

    @Test
    void register_returningPatient_reusesPatient() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient existingPatient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");

        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(treatmentTypeRepository.findById(1L)).thenReturn(Optional.of(treatment));
        when(patientRepository.findByContactNumber("0771234566")).thenReturn(Optional.of(existingPatient));
        when(appointmentRepository.count()).thenReturn(0L);
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentResponse response = appointmentService.register(validRequest());

        assertThat(response.getPatientName()).isEqualTo("Kamal");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void register_doubleBooking_rejected() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        AppointmentRequest request = validRequest();

        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(treatmentTypeRepository.findById(1L)).thenReturn(Optional.of(treatment));
        when(appointmentRepository.existsByDentistIdAndDateAndTimeAndStatus(
                1L, request.getDate(), request.getTime(), Appointment.AppointmentStatus.SCHEDULED))
                .thenReturn(true);

        assertThatThrownBy(() -> appointmentService.register(request))
                .isInstanceOf(DoubleBookingException.class);

        verify(patientRepository, never()).save(any(Patient.class));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void register_unknownDentist_rejected() {
        when(dentistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.register(validRequest()))
                .isInstanceOf(DentistNotFoundException.class);
    }

    @Test
    void register_unknownTreatment_rejected() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        when(treatmentTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.register(validRequest()))
                .isInstanceOf(TreatmentTypeNotFoundException.class);
    }

    //test search Appointment
    @Test
    void findByNo_existingNo_found() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        Appointment appointment = new Appointment(1L, "APT-00001", patient, dentist, treatment,
                LocalDate.now().plusDays(1), LocalTime.of(10, 30), Appointment.AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findByAppointmentNo("APT-00001")).thenReturn(Optional.of(appointment));

        AppointmentResponse response = appointmentService.findByAppointmentNo("APT-00001");

        assertThat(response.getAppointmentNo()).isEqualTo("APT-00001");
        assertThat(response.getPatientName()).isEqualTo("Kamal");
    }

    @Test
    void findByNo_unknownNo_rejected() {
        when(appointmentRepository.findByAppointmentNo("APT-99999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.findByAppointmentNo("APT-99999"))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    //test list all appointments
    @Test
    void findAll_hasAppointments_returnsList() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist");
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        Appointment appointment = new Appointment(1L, "APT-00001", patient, dentist, treatment,
                LocalDate.now().plusDays(1), LocalTime.of(10, 30), Appointment.AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<AppointmentResponse> responses = appointmentService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getAppointmentNo()).isEqualTo("APT-00001");
    }

    @Test
    void findAll_empty_returnsEmptyList() {
        when(appointmentRepository.findAll()).thenReturn(List.of());

        List<AppointmentResponse> responses = appointmentService.findAll();

        assertThat(responses).isEmpty();
    }
}
