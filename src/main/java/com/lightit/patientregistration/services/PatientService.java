package com.lightit.patientregistration.services;

import com.lightit.patientregistration.dtos.PatientDTO;
import com.lightit.patientregistration.dtos.PatientRegistrationRequestDTO;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PatientService {
    PatientDTO savePatient(String name, String email, String phone, MultipartFile documentPhoto) throws IOException, MessagingException;
    Page<PatientDTO> getPatients(Pageable pageable, String name);
    void deletePatient(Long id);
    PatientDTO updatePatient(Long id, PatientRegistrationRequestDTO patientDTO);
}
