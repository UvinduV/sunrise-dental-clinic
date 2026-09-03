package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.response.BillResponse;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.TreatmentType;
import com.sunrise.dentalclinic.exception.AppointmentNotFoundException;
import com.sunrise.dentalclinic.exception.BillNotFoundException;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.service.factory.BillFactory;
import com.sunrise.dentalclinic.service.fee.FeeCalculationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillFactory billFactory;

    @Mock
    private FeeCalculationStrategy feeCalculationStrategy;

    @InjectMocks
    private BillService billService;

    private Appointment sampleAppointment() {
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist", new BigDecimal("500.00"));
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000.00"));
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        return new Appointment(1L, "APT-00001", patient, dentist, treatment,
                LocalDate.now().plusDays(1), LocalTime.of(10, 30), Appointment.AppointmentStatus.SCHEDULED);
    }

    @Test
    void generateBill_existingAppointment_saved() {
        Appointment appointment = sampleAppointment();
        BigDecimal total = new BigDecimal("5500.00");
        Bill bill = new Bill(1L, appointment, total, LocalDate.now());

        when(appointmentRepository.findByAppointmentNo("APT-00001")).thenReturn(Optional.of(appointment));
        when(feeCalculationStrategy.calculateFee(appointment.getTreatment(), appointment.getDentist())).thenReturn(total);
        when(billFactory.createBill(appointment, total)).thenReturn(bill);
        when(billRepository.save(bill)).thenReturn(bill);

        BillResponse response = billService.generateBill("APT-00001");

        assertThat(response.getAppointmentNo()).isEqualTo("APT-00001");
        assertThat(response.getPatientName()).isEqualTo("Kamal");
        assertThat(response.getTreatmentFee()).isEqualByComparingTo("5000.00");
        assertThat(response.getConsultationFee()).isEqualByComparingTo("500.00");
        assertThat(response.getTotalAmount()).isEqualByComparingTo("5500.00");
    }

    @Test
    void generateBill_unknownAppointment_rejected() {
        when(appointmentRepository.findByAppointmentNo("APT-99999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.generateBill("APT-99999"))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void findById_existingBill_found() {
        Appointment appointment = sampleAppointment();
        Bill bill = new Bill(1L, appointment, new BigDecimal("5500.00"), LocalDate.now());

        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));

        BillResponse response = billService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTotalAmount()).isEqualByComparingTo("5500.00");
    }

    @Test
    void findById_unknownBill_rejected() {
        when(billRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.findById(99L))
                .isInstanceOf(BillNotFoundException.class);
    }
}
