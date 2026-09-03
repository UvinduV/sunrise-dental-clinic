package com.sunrise.dentalclinic.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigManagerTest {

    @Test
    void getInstance_twice_sameInstance() {
        AppConfigManager first = AppConfigManager.getInstance();
        AppConfigManager second = AppConfigManager.getInstance();

        assertThat(first).isSameAs(second);
    }

    @Test
    void getInstance_returnsConfigValues() {
        AppConfigManager config = AppConfigManager.getInstance();

        assertThat(config.getClinicName()).isEqualTo("Sunrise Dental Clinic");
        assertThat(config.getConsultationFee()).isEqualByComparingTo("500.00");
    }
}
