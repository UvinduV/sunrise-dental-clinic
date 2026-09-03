package com.sunrise.dentalclinic.service.factory;

import com.sunrise.dentalclinic.dto.response.AppointmentResponseDTO;
import com.sunrise.dentalclinic.dto.response.DailyAppointmentsReportResponseDTO;
import com.sunrise.dentalclinic.dto.response.RevenueReportResponseDTO;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReportFactory {

    public DailyAppointmentsReportResponseDTO createDailyAppointmentsReport(LocalDate date, List<Appointment> appointments) {
        List<AppointmentResponseDTO> appointmentResponses = appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();

        return new DailyAppointmentsReportResponseDTO(date, appointmentResponses.size(), appointmentResponses);
    }

    public RevenueReportResponseDTO createRevenueReport(LocalDate from, LocalDate to, List<Bill> bills) {
        BigDecimal totalRevenue = bills.stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RevenueReportResponseDTO(from, to, bills.size(), totalRevenue);
    }

    private AppointmentResponseDTO toAppointmentResponse(Appointment appointment) {
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
