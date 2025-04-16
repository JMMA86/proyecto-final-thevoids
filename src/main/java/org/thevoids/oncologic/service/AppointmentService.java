package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Appointment;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Long id);

    void createAppointment(Long patientId, Long ClinicAssigmentId, Long appointmentTypeId, Date dateTime);

    Appointment updateAppointment(Appointment appointment);

    void deleteAppointment(Long id);
}
