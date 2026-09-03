package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.response.DailyAppointmentsReportResponseDTO;
import com.sunrise.dentalclinic.dto.response.RevenueReportResponseDTO;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.service.factory.ReportFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final ReportFactory reportFactory;

    public DailyAppointmentsReportResponseDTO getDailyAppointmentsReport(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        return reportFactory.createDailyAppointmentsReport(date, appointments);
    }

    public RevenueReportResponseDTO getRevenueReport(LocalDate from, LocalDate to) {
        List<Bill> bills = billRepository.findByIssuedDateBetween(from, to);
        return reportFactory.createRevenueReport(from, to, bills);
    }
}
