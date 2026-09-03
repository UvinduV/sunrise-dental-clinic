package com.sunrise.dentalclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpSectionResponseDTO {
    private String title;
    private List<String> steps;
}
