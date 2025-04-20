package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.repository.AppointmentTypeRepository;
import org.thevoids.oncologic.service.AppointmentTypeService;

import java.util.List;

@Service
public class AppointmentTypeServiceImpl implements AppointmentTypeService {
    private final AppointmentTypeRepository appointmentTypeRepository;

    public AppointmentTypeServiceImpl(AppointmentTypeRepository appointmentTypeRepository) {
        this.appointmentTypeRepository = appointmentTypeRepository;
    }

    @Override
    public AppointmentType createAppointmentType(AppointmentType appointmentType) {
        if (appointmentType == null) {
            throw new IllegalArgumentException("AppointmentType cannot be null");
        }

        if (appointmentType.getTypeId() != null) {
            throw new IllegalArgumentException("New AppointmentType should not have an ID");
        }

        if (appointmentType.getTypeName() == null || appointmentType.getTypeName().isEmpty()) {
            throw new IllegalArgumentException("AppointmentType name cannot be null or empty");
        }

        if (appointmentType.getStandardDuration() == null || appointmentType.getStandardDuration() <= 0) {
            throw new IllegalArgumentException("AppointmentType standard duration must be a positive integer");
        }

        appointmentTypeRepository.save(appointmentType);

        return appointmentType;
    }

    @Override
    public AppointmentType getAppointmentTypeById(Long id) {
        return appointmentTypeRepository.findById(id).orElse(null);
    }

    @Override
    public AppointmentType updateAppointmentType(AppointmentType appointmentType) {
        if (appointmentType == null) {
            throw new IllegalArgumentException("AppointmentType cannot be null");
        }

        if (appointmentType.getTypeId() == null) {
            throw new IllegalArgumentException("AppointmentType ID cannot be null");
        }

        AppointmentType existingAppointmentType = appointmentTypeRepository.findById(appointmentType.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("AppointmentType with ID " + appointmentType.getTypeId() + " does not exist"));

        existingAppointmentType.setTypeName(appointmentType.getTypeName());
        existingAppointmentType.setStandardDuration(appointmentType.getStandardDuration());

        return appointmentTypeRepository.save(existingAppointmentType);
    }

    @Override
    public void deleteAppointmentType(Long id) {
        if (!appointmentTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("AppointmentType with id " + id + " does not exist");
        }

        appointmentTypeRepository.deleteById(id);
    }

    @Override
    public List<AppointmentType> getAllAppointmentTypes() {
        return appointmentTypeRepository.findAll();
    }
}
