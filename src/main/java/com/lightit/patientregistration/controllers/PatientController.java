package com.lightit.patientregistration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightit.patientregistration.dtos.PatientDTO;
import com.lightit.patientregistration.dtos.PatientRegistrationRequestDTO;
import com.lightit.patientregistration.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "API for patient operations")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Register a new patient")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PatientDTO registerPatient(@Valid @ModelAttribute PatientRegistrationRequestDTO request,
                                      @RequestParam(value = "documentPhoto", required = false) MultipartFile documentPhoto) throws IOException, MessagingException {
        return patientService.savePatient(request.getName(), request.getEmail(), request.getPhone(), documentPhoto);
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public Page<PatientDTO> getPatients(@PageableDefault(size = 5) Pageable pageable,
                                        @RequestParam(required = false) String name) {
        return patientService.getPatients(pageable, name);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient")
    public PatientDTO updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRegistrationRequestDTO patientDTO) {
        return patientService.updatePatient(id, patientDTO);
    }
}
