package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.repository.AppointmentRepository;
import org.thevoids.oncologic.repository.AppointmentTypeRepository;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.service.AppointmentService;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTypeRepository appointmentTypeRepository;

    private final PatientRepository patientRepository;

    private final ClinicAssignmentRepository clinicAssignmentRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            AppointmentTypeRepository appointmentTypeRepository,
            PatientRepository patientRepository,
            ClinicAssignmentRepository clinicAssignmentRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
        this.patientRepository = patientRepository;
        this.clinicAssignmentRepository = clinicAssignmentRepository;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with id " + id + " does not exist"));
    }

    @Override
    public void createAppointment(Long patientId, Long clinicAssignmentId, Long appointmentTypeId, Date dateTime) {
        if (patientId == null || clinicAssignmentId == null || appointmentTypeId == null || dateTime == null) {
            StringBuilder errorMessage = new StringBuilder("Missing required parameter(s): ");

            if (patientId == null) errorMessage.append("patientId, ");
            if (clinicAssignmentId == null) errorMessage.append("clinicAssignmentId, ");
            if (appointmentTypeId == null) errorMessage.append("appointmentTypeId, ");
            if (dateTime == null) errorMessage.append("dateTime, ");

            // Remove trailing comma and space
            errorMessage.setLength(errorMessage.length() - 2);

            throw new IllegalArgumentException(errorMessage.toString());
        }

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new IllegalArgumentException("Patient with id " + patientId + " does not exist")
        );

        ClinicAssignment clinicAssignment = clinicAssignmentRepository.findById(clinicAssignmentId).orElseThrow(
                () -> new IllegalArgumentException("ClinicAssignment with id " + clinicAssignmentId + " does not exist")
        );

        AppointmentType appointmentType = appointmentTypeRepository.findById(appointmentTypeId).orElseThrow(
                () -> new IllegalArgumentException("AppointmentType with id " + appointmentTypeId + " does not exist")
        );

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setClinicAssignment(clinicAssignment);
        appointment.setAppointmentType(appointmentType);
        appointment.setDateTime(dateTime);
        appointment.setDoctor(clinicAssignment.getUser());

        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }

        if (appointment.getAppointmentId() == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }

        if (!appointmentRepository.existsById(appointment.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with id " + appointment.getAppointmentId() + " does not exist");
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Appointment with id " + id + " does not exist");
        }

        appointmentRepository.deleteById(id);
    }
}