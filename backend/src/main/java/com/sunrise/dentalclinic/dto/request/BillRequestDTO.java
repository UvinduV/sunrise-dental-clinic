package com.sunrise.dentalclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillRequestDTO {

    @NotBlank(message = "Appointment number is required")
    private String appointmentNo;
}
