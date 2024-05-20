package com.lightit.patientregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String documentPhotoPath;
}
