package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.PatientMapper;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return patientMapper.toPatientDTO(patient);
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new InvalidOperationException("Patient cannot be null");
        }

        Patient patient = patientMapper.toPatient(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(savedPatient);
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new InvalidOperationException("Patient cannot be null");
        }

        if (patientDTO.getPatientId() == null) {
            throw new InvalidOperationException("Patient ID cannot be null");
        }

        Patient patient = patientRepository.findById(patientDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientDTO.getPatientId()));

        // Update only the fields from DTO (do not replace the entity)
        patient.setBloodGroup(patientDTO.getBloodGroup());
        patient.setAllergies(patientDTO.getAllergies());
        patient.setFamilyHistory(patientDTO.getFamilyHistory());
        // Do not update user or child collections here

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        if (patient.getAppointments() != null) {
            for (var appointment : patient.getAppointments()) {
                if (appointment.getTasks() != null) {
                    appointment.getTasks().clear();
                }
            }
        }
        patientRepository.delete(patient);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toPatientDTO)
                .collect(Collectors.toList());
    }
}
