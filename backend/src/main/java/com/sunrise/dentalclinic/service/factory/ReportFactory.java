package com.sunrise.dentalclinic.service.factory;

import com.sunrise.dentalclinic.dto.response.AppointmentResponse;
import com.sunrise.dentalclinic.dto.response.DailyAppointmentsReportResponse;
import com.sunrise.dentalclinic.dto.response.RevenueReportResponse;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReportFactory {

    public DailyAppointmentsReportResponse createDailyAppointmentsReport(LocalDate date, List<Appointment> appointments) {
        List<AppointmentResponse> appointmentResponses = appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();

        return new DailyAppointmentsReportResponse(date, appointmentResponses.size(), appointmentResponses);
    }

    public RevenueReportResponse createRevenueReport(LocalDate from, LocalDate to, List<Bill> bills) {
        BigDecimal totalRevenue = bills.stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RevenueReportResponse(from, to, bills.size(), totalRevenue);
    }

    private AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return new AppointmentResponse(
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
