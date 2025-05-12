package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.entity.Patient;
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
                .orElseThrow(() -> new IllegalArgumentException("Patient with id " + id + " does not exist"));
        return patientMapper.toPatientDTO(patient);
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        Patient patient = patientMapper.toPatient(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(savedPatient);
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) {
        if (patientDTO == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (patientDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        if (!patientRepository.existsById(patientDTO.getPatientId())) {
            throw new IllegalArgumentException("Patient with id " + patientDTO.getPatientId() + " does not exist");
        }

        Patient patient = patientMapper.toPatient(patientDTO);
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient with id " + id + " does not exist");
        }

        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toPatientDTO)
                .collect(Collectors.toList());
    }
}
