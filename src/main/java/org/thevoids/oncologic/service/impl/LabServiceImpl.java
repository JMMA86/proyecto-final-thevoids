package org.thevoids.oncologic.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.entity.Lab;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.LabMapper;
import org.thevoids.oncologic.repository.LabRepository;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.FileService;
import org.thevoids.oncologic.service.LabService;

import jakarta.transaction.Transactional;

@Service
public class LabServiceImpl implements LabService {
    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final LabMapper labMapper;
    private final FileService fileService;

    public LabServiceImpl(
            LabRepository labRepository,
            UserRepository userRepository,
            PatientRepository patientRepository,
            LabMapper labMapper,
            FileService fileService) {
        this.labRepository = labRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.labMapper = labMapper;
        this.fileService = fileService;
    }

    @Override
    public LabDTO getLabById(Long id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab", "id", id));
        return labMapper.toLabDTO(lab);
    }

    @Override
    public LabDTO updateLab(LabDTO labDTO) {
        if (labDTO == null) {
            throw new InvalidOperationException("Lab cannot be null");
        }

        if (labDTO.getLabId() == null) {
            throw new InvalidOperationException("Lab ID cannot be null");
        }

        if (!labRepository.existsById(labDTO.getLabId())) {
            throw new ResourceNotFoundException("Lab", "id", labDTO.getLabId());
        }

        Lab lab = labMapper.toLab(labDTO);

        // Fetch the complete entities to avoid null references
        if (lab.getPatient() != null && lab.getPatient().getPatientId() != null) {
            Patient patient = patientRepository.findById(lab.getPatient().getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient", "id", lab.getPatient().getPatientId()));
            lab.setPatient(patient);
        }

        if (lab.getLabTechnician() != null && lab.getLabTechnician().getUserId() != null) {
            User user = userRepository.findById(lab.getLabTechnician().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User", "id", lab.getLabTechnician().getUserId()));
            lab.setLabTechnician(user);
        }

        Lab savedLab = labRepository.save(lab);
        return labMapper.toLabDTO(savedLab);
    }

    @Override
    public List<LabDTO> getAllLabs() {
        List<Lab> labs = labRepository.findAll();
        return labs.stream()
                .map(labMapper::toLabDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LabDTO assignLab(Long patientId, Long userId, Date requestDate, String testType, Date completionDate,
            String result) {
        if (patientId == null || userId == null || requestDate == null) {
            throw new InvalidOperationException("Patient ID, User ID, and Request Date cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        Lab lab = new Lab();
        lab.setRequestDate(requestDate);
        lab.setLabTechnician(user);
        lab.setPatient(patient);
        lab.setTestType(testType);
        lab.setCompletionDate(completionDate);
        lab.setResult(result);

        Lab savedLab = labRepository.save(lab);
        return labMapper.toLabDTO(savedLab);
    }

    @Override
    @Transactional
    public LabDTO updateLabWithFile(LabDTO labDTO, MultipartFile file) {
        if (labDTO == null) {
            throw new InvalidOperationException("Lab cannot be null");
        }

        if (labDTO.getLabId() == null) {
            throw new InvalidOperationException("Lab ID cannot be null");
        }

        Lab existingLab = labRepository.findById(labDTO.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab", "id", labDTO.getLabId()));

        // Handle file upload if provided
        if (file != null && !file.isEmpty()) {
            // Validate file
            if (!fileService.isValidFile(file)) {
                throw new InvalidOperationException("Invalid file type or size");
            }

            // Delete old file if exists
            if (existingLab.getAttachment() != null && !existingLab.getAttachment().isEmpty()) {
                fileService.deleteFile(existingLab.getAttachment());
            }

            // Store new file
            String filePath = fileService.storeFile(file, "labs");
            labDTO.setAttachment(filePath);
        }

        Lab lab = labMapper.toLab(labDTO);

        // Fetch the complete entities to avoid null references
        if (lab.getPatient() != null && lab.getPatient().getPatientId() != null) {
            Patient patient = patientRepository.findById(lab.getPatient().getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Patient", "id", lab.getPatient().getPatientId()));
            lab.setPatient(patient);
        }

        if (lab.getLabTechnician() != null && lab.getLabTechnician().getUserId() != null) {
            User user = userRepository.findById(lab.getLabTechnician().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User", "id", lab.getLabTechnician().getUserId()));
            lab.setLabTechnician(user);
        }

        Lab savedLab = labRepository.save(lab);
        return labMapper.toLabDTO(savedLab);
    }

    @Override
    @Transactional
    public LabDTO assignLabWithFile(Long patientId, Long userId, Date requestDate, String testType, Date completionDate,
            String result, MultipartFile file) {
        if (patientId == null || userId == null || requestDate == null) {
            throw new InvalidOperationException("Patient ID, User ID, and Request Date cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        Lab lab = new Lab();
        lab.setRequestDate(requestDate);
        lab.setLabTechnician(user);
        lab.setPatient(patient);
        lab.setTestType(testType);
        lab.setCompletionDate(completionDate);
        lab.setResult(result);

        // Handle file upload if provided
        if (file != null && !file.isEmpty()) {
            // Validate file
            if (!fileService.isValidFile(file)) {
                throw new InvalidOperationException("Invalid file type or size");
            }

            // Store file
            String filePath = fileService.storeFile(file, "labs");
            lab.setAttachment(filePath);
        }

        Lab savedLab = labRepository.save(lab);
        return labMapper.toLabDTO(savedLab);
    }

    @Override
    public void deleteLab(Long id) {
        Lab lab = labRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab", "id", id));

        // Delete associated file if exists
        if (lab.getAttachment() != null && !lab.getAttachment().isEmpty()) {
            fileService.deleteFile(lab.getAttachment());
        }

        labRepository.deleteById(id);
    }
}
