package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.dto.response.BillResponse;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.exception.AppointmentNotFoundException;
import com.sunrise.dentalclinic.exception.BillNotFoundException;
import com.sunrise.dentalclinic.repository.AppointmentRepository;
import com.sunrise.dentalclinic.repository.BillRepository;
import com.sunrise.dentalclinic.service.factory.BillFactory;
import com.sunrise.dentalclinic.service.fee.FeeCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BillService {

    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final BillFactory billFactory;
    private final FeeCalculationStrategy feeCalculationStrategy;

    @Transactional
    public BillResponse generateBill(String appointmentNo) {
        Appointment appointment = appointmentRepository.findByAppointmentNo(appointmentNo)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "No appointment found with appointment number: " + appointmentNo));

        BigDecimal totalAmount = feeCalculationStrategy.calculateFee(appointment.getTreatment(), appointment.getDentist());

        Bill bill = billFactory.createBill(appointment, totalAmount);
        bill = billRepository.save(bill);

        return toResponse(bill);
    }

    public BillResponse findById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("No bill found with id: " + id));

        return toResponse(bill);
    }

    private BillResponse toResponse(Bill bill) {
        Appointment appointment = bill.getAppointment();
        BigDecimal treatmentFee = appointment.getTreatment().getFee();
        BigDecimal consultationFee = bill.getTotalAmount().subtract(treatmentFee);

        return new BillResponse(
                bill.getId(),
                appointment.getAppointmentNo(),
                appointment.getPatient().getName(),
                appointment.getTreatment().getName(),
                treatmentFee,
                consultationFee,
                bill.getTotalAmount(),
                bill.getIssuedDate()
        );
    }
}
