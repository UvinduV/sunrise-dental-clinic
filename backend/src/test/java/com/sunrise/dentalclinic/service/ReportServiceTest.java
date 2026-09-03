package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.response.DailyAppointmentsReportResponse;
import com.sunrise.dentalclinic.dto.response.RevenueReportResponse;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.TreatmentType;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.service.factory.ReportFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private BillRepository billRepository;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService(appointmentRepository, billRepository, new ReportFactory());
    }

    @Test
    void dailyReport_hasAppointments_correctCount() {
        LocalDate date = LocalDate.of(2026, 9, 10);
        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist", new BigDecimal("500.00"));
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        Appointment appointment = new Appointment(1L, "APT-00001", patient, dentist, treatment,
                date, LocalTime.of(10, 30), Appointment.AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findByDate(date)).thenReturn(List.of(appointment));

        DailyAppointmentsReportResponse response = reportService.getDailyAppointmentsReport(date);

        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getTotalAppointments()).isEqualTo(1);
        assertThat(response.getAppointments().get(0).getAppointmentNo()).isEqualTo("APT-00001");
    }

    @Test
    void dailyReport_empty_zeroCount() {
        LocalDate date = LocalDate.of(2026, 9, 11);
        when(appointmentRepository.findByDate(date)).thenReturn(Collections.emptyList());

        DailyAppointmentsReportResponse response = reportService.getDailyAppointmentsReport(date);

        assertThat(response.getTotalAppointments()).isEqualTo(0);
        assertThat(response.getAppointments()).isEmpty();
    }

    @Test
    void revenueReport_sumsBillTotals() {
        LocalDate from = LocalDate.of(2026, 9, 1);
        LocalDate to = LocalDate.of(2026, 9, 30);

        Dentist dentist = new Dentist(1L, "Dr. Silva", "Orthodontist", new BigDecimal("500.00"));
        TreatmentType treatment = new TreatmentType(1L, "Root Canal", new BigDecimal("5000"));
        Patient patient = new Patient(1L, "Kamal", "123 Main St, Colombo", "0771234566");
        Appointment appointment = new Appointment(1L, "APT-00001", patient, dentist, treatment,
                from, LocalTime.of(10, 30), Appointment.AppointmentStatus.SCHEDULED);

        Bill bill1 = new Bill(1L, appointment, new BigDecimal("5500.00"), from);
        Bill bill2 = new Bill(2L, appointment, new BigDecimal("3200.00"), from);

        when(billRepository.findByIssuedDateBetween(from, to)).thenReturn(List.of(bill1, bill2));

        RevenueReportResponse response = reportService.getRevenueReport(from, to);

        assertThat(response.getTotalBills()).isEqualTo(2);
        assertThat(response.getTotalRevenue()).isEqualByComparingTo("8700.00");
    }
}
