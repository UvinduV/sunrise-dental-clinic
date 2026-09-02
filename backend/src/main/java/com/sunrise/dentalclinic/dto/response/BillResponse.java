package com.sunrise.dentalclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private Long id;
    private String appointmentNo;
    private String patientName;
    private String treatmentName;
    private BigDecimal treatmentFee;
    private BigDecimal consultationFee;
    private BigDecimal totalAmount;
    private LocalDate issuedDate;
}
