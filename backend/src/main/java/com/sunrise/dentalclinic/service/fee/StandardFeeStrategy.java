package com.sunrise.dentalclinic.service.fee;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.TreatmentType;
import com.sunrise.dentalclinic.util.AppConfigManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StandardFeeStrategy implements FeeCalculationStrategy {

    @Override
    public BigDecimal calculateFee(TreatmentType treatment, Dentist dentist) {
        BigDecimal consultationFee = dentist.getConsultationFee() != null
                ? dentist.getConsultationFee()
                : AppConfigManager.getInstance().getConsultationFee();

        return treatment.getFee().add(consultationFee);
    }
}
