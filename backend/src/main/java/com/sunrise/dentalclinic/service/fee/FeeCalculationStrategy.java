package com.sunrise.dentalclinic.service.fee;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.TreatmentType;

import java.math.BigDecimal;

public interface FeeCalculationStrategy {
    BigDecimal calculateFee(TreatmentType treatment, Dentist dentist);
}
