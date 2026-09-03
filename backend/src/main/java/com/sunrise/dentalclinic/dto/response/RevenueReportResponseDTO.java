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
public class RevenueReportResponseDTO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private int totalBills;
    private BigDecimal totalRevenue;
}
