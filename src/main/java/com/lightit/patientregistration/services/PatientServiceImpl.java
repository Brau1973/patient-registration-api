package com.lightit.patientregistration.services;

import com.lightit.patientregistration.dtos.PatientDTO;
import com.lightit.patientregistration.dtos.PatientRegistrationRequestDTO;
import com.lightit.patientregistration.exceptions.CreateUploadsDirectoryFailedException;
import com.lightit.patientregistration.exceptions.MaxUploadSizeExceededException;
import com.lightit.patientregistration.exceptions.InvalidFileExtensionException;
import com.lightit.patientregistration.exceptions.ResourceNotFoundException;
import com.lightit.patientregistration.models.Patient;
import com.lightit.patientregistration.repositories.PatientRepository;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "png");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes
    private static final double MAX_FILE_SIZE_MB = MAX_FILE_SIZE / (1024.0 * 1024.0); // 10MB in megabytes

    private final String UPLOAD_DIR = "uploads/";
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, ModelMapper modelMapper, EmailService emailService) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public PatientDTO savePatient(String name, String email, String phone, MultipartFile documentPhoto) throws IOException, MessagingException {
        Path filePath = null;
        if (documentPhoto != null && !documentPhoto.isEmpty()) {
            validateFileSize(documentPhoto);
            String originalFilename = documentPhoto.getOriginalFilename();
            validateFileExtension(originalFilename);
            createUploadsDirectory();
            filePath = saveDocumentPhoto(documentPhoto, originalFilename);
        }

        try {
            Patient patient = new Patient(0L, name, email, phone, filePath != null ? filePath.toString() : null);
            Patient savedPatient = patientRepository.save(patient);

            emailService.sendConfirmationEmail(savedPatient.getEmail(), savedPatient.getName());

            return modelMapper.map(savedPatient, PatientDTO.class);
        } catch (Exception e) {
            if (filePath != null) {
                Files.delete(filePath);
            }
            throw e;
        }
    }

    @Override
    public Page<PatientDTO> getPatients(Pageable pageable, String name) {
        Specification<Patient> spec = (root, query, cb) -> name == null ? cb.conjunction() : cb.like(root.get("name"), "%" + name + "%");
        Page<Patient> patients = patientRepository.findAll(spec, pageable);
        return patients.map(patient -> modelMapper.map(patient, PatientDTO.class));
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        if (patient != null) {
            patientRepository.deleteById(id);
            if (patient.getDocumentPhotoPath() != null) {
                try {
                    Path filePath = Paths.get(patient.getDocumentPhotoPath());
                    Files.delete(filePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete file", e);
                }
            }
        }
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientRegistrationRequestDTO patientDTO) {
        return patientRepository.findById(id)
                .map(existingPatient -> {
                    if (patientDTO.getName() != null && !patientDTO.getName().isEmpty()) {
                        existingPatient.setName(patientDTO.getName());
                    }
                    if (patientDTO.getEmail() != null && !patientDTO.getEmail().isEmpty()) {
                        existingPatient.setEmail(patientDTO.getEmail());
                    }
                    if (patientDTO.getPhone() != null && !patientDTO.getPhone().isEmpty()) {
                        existingPatient.setPhoneNumber(patientDTO.getPhone());
                    }
                    Patient updatedPatient = patientRepository.save(existingPatient);
                    return modelMapper.map(updatedPatient, PatientDTO.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    private void validateFileSize(MultipartFile documentPhoto) {
        if (documentPhoto.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException("The file exceeds the maximum allowed size of " + MAX_FILE_SIZE_MB + " MB");
        }
    }

    private void validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new InvalidFileExtensionException("The file extension is not valid. Allowed extensions are " + ALLOWED_EXTENSIONS);
        }
    }

    private void createUploadsDirectory() {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()){
            boolean isDirectoryCreated = directory.mkdir();
            if (!isDirectoryCreated) {
                throw new CreateUploadsDirectoryFailedException("Failed to create directory " + UPLOAD_DIR);
            }
        }
    }

    private Path saveDocumentPhoto(MultipartFile documentPhoto, String originalFilename) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.copy(documentPhoto.getInputStream(), filePath);
        return filePath;
    }
}



