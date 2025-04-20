package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.AppointmentType;

import java.util.List;

public interface AppointmentTypeService {
    // Define the methods that will be implemented by the service class
    AppointmentType createAppointmentType(AppointmentType appointmentType);

    AppointmentType getAppointmentTypeById(Long id);

    AppointmentType updateAppointmentType(AppointmentType appointmentType);

    void deleteAppointmentType(Long id);

    List<AppointmentType> getAllAppointmentTypes();
}
