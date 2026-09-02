package com.sunrise.dentalclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyAppointmentsReportResponse {
    private LocalDate date;
    private int totalAppointments;
    private List<AppointmentResponse> appointments;
}
