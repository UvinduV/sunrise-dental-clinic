package com.sunrise.dentalclinic.service.fee;

import com.sunrise.dentalclinic.entity.TreatmentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// Default strategy: total = treatment fee + consultation fee
@Component
public class StandardFeeStrategy implements FeeCalculationStrategy {

    private static final BigDecimal CONSULTATION_FEE = new BigDecimal("500.00");

    @Override
    public BigDecimal calculateFee(TreatmentType treatment) {
        return treatment.getFee().add(CONSULTATION_FEE);
    }

    public BigDecimal getConsultationFee() {
        return CONSULTATION_FEE;
    }
}
