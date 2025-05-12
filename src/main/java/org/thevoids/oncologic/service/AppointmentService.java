package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.Appointment;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Long id);

    Appointment createAppointment(Long patientId, Long ClinicAssigmentId, Long appointmentTypeId, Date dateTime);

    Appointment updateAppointment(Appointment appointment); // Updates only the attributes of the existing entity

    void deleteAppointment(Long id);
}
