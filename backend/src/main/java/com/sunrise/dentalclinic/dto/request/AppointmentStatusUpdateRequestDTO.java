package com.sunrise.dentalclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "SCHEDULED|COMPLETED|CANCELLED")
    private String status;
}
