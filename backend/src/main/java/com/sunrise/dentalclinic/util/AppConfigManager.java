package com.sunrise.dentalclinic.util;

import java.math.BigDecimal;

// Singleton pattern
public final class AppConfigManager {

    private static AppConfigManager instance;

    private final String clinicName;
    private final BigDecimal consultationFee;

    private AppConfigManager() {
        this.clinicName = "Sunrise Dental Clinic";
        this.consultationFee = new BigDecimal("500.00");
    }

    public static synchronized AppConfigManager getInstance() {
        if (instance == null) {
            instance = new AppConfigManager();
        }
        return instance;
    }

    public String getClinicName() {
        return clinicName;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }
}
