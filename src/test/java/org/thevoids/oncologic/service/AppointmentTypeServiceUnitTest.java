package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.repository.AppointmentTypeRepository;
import org.thevoids.oncologic.service.impl.AppointmentTypeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentTypeServiceUnitTest {

    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;

    @InjectMocks
    private AppointmentTypeServiceImpl appointmentTypeService;

    @Test
    void createAppointmentTypeSuccessfully() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(30);

        when(appointmentTypeRepository.save(any(AppointmentType.class))).thenReturn(appointmentType);

        AppointmentType result = appointmentTypeService.createAppointmentType(appointmentType);

        assertNotNull(result);
        assertEquals("Consultation", result.getTypeName());
        assertEquals(30, result.getStandardDuration());
        verify(appointmentTypeRepository).save(appointmentType);
    }

    @Test
    void createAppointmentTypeWithNullThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(null));

        assertEquals("AppointmentType cannot be null", exception.getMessage());
    }

    @Test
    void createAppointmentTypeWithExistingIdThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeId(1L);
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(appointmentType));

        assertEquals("New AppointmentType should not have an ID", exception.getMessage());
    }

    @Test
    void createAppointmentTypeWithNullNameThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setStandardDuration(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(appointmentType));

        assertEquals("AppointmentType name cannot be null or empty", exception.getMessage());
    }

    @Test
    void createAppointmentTypeWithEmptyNameThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeName("");
        appointmentType.setStandardDuration(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(appointmentType));

        assertEquals("AppointmentType name cannot be null or empty", exception.getMessage());
    }

    @Test
    void createAppointmentTypeWithNullDurationThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeName("Consultation");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(appointmentType));

        assertEquals("AppointmentType standard duration must be a positive integer", exception.getMessage());
    }

    @Test
    void createAppointmentTypeWithZeroDurationThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.createAppointmentType(appointmentType));

        assertEquals("AppointmentType standard duration must be a positive integer", exception.getMessage());
    }

    @Test
    void getAppointmentTypeByIdReturnsAppointmentType() {
        Long id = 1L;
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeId(id);
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(30);

        when(appointmentTypeRepository.findById(id)).thenReturn(Optional.of(appointmentType));

        AppointmentType result = appointmentTypeService.getAppointmentTypeById(id);

        assertNotNull(result);
        assertEquals(id, result.getTypeId());
        assertEquals("Consultation", result.getTypeName());
        assertEquals(30, result.getStandardDuration());
    }

    @Test
    void getAppointmentTypeByIdReturnsNullWhenNotFound() {
        Long id = 1L;

        when(appointmentTypeRepository.findById(id)).thenReturn(Optional.empty());

        AppointmentType result = appointmentTypeService.getAppointmentTypeById(id);

        assertNull(result);
    }

    @Test
    void updateAppointmentTypeSuccessfully() {
        Long id = 1L;
        AppointmentType existingAppointmentType = new AppointmentType();
        existingAppointmentType.setTypeId(id);
        existingAppointmentType.setTypeName("Consultation");
        existingAppointmentType.setStandardDuration(30);

        AppointmentType updatedAppointmentType = new AppointmentType();
        updatedAppointmentType.setTypeId(id);
        updatedAppointmentType.setTypeName("Extended Consultation");
        updatedAppointmentType.setStandardDuration(45);

        when(appointmentTypeRepository.findById(id)).thenReturn(Optional.of(existingAppointmentType));
        when(appointmentTypeRepository.save(any(AppointmentType.class))).thenReturn(existingAppointmentType);

        AppointmentType result = appointmentTypeService.updateAppointmentType(updatedAppointmentType);

        assertNotNull(result);
        assertEquals("Extended Consultation", result.getTypeName());
        assertEquals(45, result.getStandardDuration());
    }

    @Test
    void updateAppointmentTypeWithNullThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.updateAppointmentType(null));

        assertEquals("AppointmentType cannot be null", exception.getMessage());
    }

    @Test
    void updateAppointmentTypeWithNullIdThrowsException() {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(30);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.updateAppointmentType(appointmentType));

        assertEquals("AppointmentType ID cannot be null", exception.getMessage());
    }

    @Test
    void updateAppointmentTypeNotFoundThrowsException() {
        Long id = 1L;
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeId(id);
        appointmentType.setTypeName("Consultation");
        appointmentType.setStandardDuration(30);

        when(appointmentTypeRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.updateAppointmentType(appointmentType));

        assertEquals("AppointmentType with ID 1 does not exist", exception.getMessage());
    }

    @Test
    void deleteAppointmentTypeSuccessfully() {
        Long id = 1L;

        when(appointmentTypeRepository.existsById(id)).thenReturn(true);

        appointmentTypeService.deleteAppointmentType(id);

        verify(appointmentTypeRepository).deleteById(id);
    }

    @Test
    void deleteAppointmentTypeNotFoundThrowsException() {
        Long id = 1L;

        when(appointmentTypeRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                appointmentTypeService.deleteAppointmentType(id));

        assertEquals("AppointmentType with id 1 does not exist", exception.getMessage());
    }

    @Test
    void getAllAppointmentTypesReturnsAllTypes() {
        List<AppointmentType> appointmentTypes = List.of(
                createAppointmentType(1L, "Consultation", 30),
                createAppointmentType(2L, "Follow-up", 15),
                createAppointmentType(3L, "Surgery", 120)
        );

        when(appointmentTypeRepository.findAll()).thenReturn(appointmentTypes);

        List<AppointmentType> result = appointmentTypeService.getAllAppointmentTypes();

        assertEquals(3, result.size());
        assertEquals("Consultation", result.get(0).getTypeName());
        assertEquals("Follow-up", result.get(1).getTypeName());
        assertEquals("Surgery", result.get(2).getTypeName());
    }

    private AppointmentType createAppointmentType(Long id, String name, Integer duration) {
        AppointmentType appointmentType = new AppointmentType();
        appointmentType.setTypeId(id);
        appointmentType.setTypeName(name);
        appointmentType.setStandardDuration(duration);
        return appointmentType;
    }
}