package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.dto.entity.PatientDTO;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.mapper.PatientMapper;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.repository.TaskRepository;
import org.thevoids.oncologic.service.PatientService;
import org.thevoids.oncologic.service.UserService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PatientMapper patientMapper;
    private final TaskRepository taskRepository;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            PatientMapper patientMapper,
            UserService userService,
            TaskRepository taskRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userService = userService;
        this.taskRepository = taskRepository;
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
        User user = userService.getUserById(patientDTO.getUserId());
        if (user.getPatient() != null) {
            throw new InvalidOperationException("User already has a patient assigned");
        }
        Patient patient = patientMapper.toPatient(patientDTO);
        patient.setUser(user);
        user.setPatient(patient); // Bidirectional
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
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        User user = patient.getUser();
        if (user != null) {
            user.setPatient(null);
            patient.setUser(null);
        }

        if (patient.getAppointments() != null) {
            patient.getAppointments().forEach(appointment -> {
                if (appointment.getTasks() != null) {
                    taskRepository.deleteAll(appointment.getTasks());
                    appointment.getTasks().clear();
                }
                appointment.setPatient(null);
            });
            patient.getAppointments().clear();
        }

        // Elimina labs asociados
        if (patient.getLabs() != null) {
            patient.getLabs().forEach(lab -> lab.setPatient(null));
            patient.getLabs().clear();
        }

        // Elimina historias médicas asociadas
        if (patient.getMedicalHistories() != null) {
            patient.getMedicalHistories().forEach(hist -> hist.setPatient(null));
            patient.getMedicalHistories().clear();
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
