package com.sunrise.dentalclinic.service.factory;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Bill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class BillFactory {

    public Bill createBill(Appointment appointment, BigDecimal totalAmount) {
        Bill bill = new Bill();
        bill.setAppointment(appointment);
        bill.setTotalAmount(totalAmount);
        bill.setIssuedDate(LocalDate.now());
        return bill;
    }
}
