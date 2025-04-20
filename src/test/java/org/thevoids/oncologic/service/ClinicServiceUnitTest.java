package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.repository.ClinicRepository;
import org.thevoids.oncologic.service.impl.ClinicServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClinicServiceUnitTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void getAllClinicsReturnsAllClinics() {
        List<Clinic> expectedClinics = List.of(
                createClinic(1L, "Clinic A", "Address A", "123456", "Specialty A", 50),
                createClinic(2L, "Clinic B", "Address B", "654321", "Specialty B", 75)
        );

        when(clinicRepository.findAll()).thenReturn(expectedClinics);

        List<Clinic> result = clinicService.getAllClinics();

        assertEquals(2, result.size());
        assertEquals(expectedClinics, result);
    }

    @Test
    void getClinicByIdReturnsClinicWhenExists() {
        Long id = 1L;
        Clinic expectedClinic = createClinic(id, "Test Clinic", "Test Address", "123456", "Oncology", 100);

        when(clinicRepository.findById(id)).thenReturn(Optional.of(expectedClinic));

        Clinic result = clinicService.getClinicById(id);

        assertNotNull(result);
        assertEquals(expectedClinic, result);
        assertEquals(id, result.getId());
        assertEquals("Test Clinic", result.getName());
    }

    @Test
    void getClinicByIdReturnsNullWhenNotExists() {
        Long id = 1L;

        when(clinicRepository.findById(id)).thenReturn(Optional.empty());

        Clinic result = clinicService.getClinicById(id);

        assertNull(result);
    }

    @Test
    void createClinicSuccessfullyCreatesClinic() {
        Clinic clinic = createClinic(null, "New Clinic", "New Address", "123456", "Cardiology", 75);
        Clinic savedClinic = createClinic(1L, "New Clinic", "New Address", "123456", "Cardiology", 75);

        when(clinicRepository.save(clinic)).thenReturn(savedClinic);

        Clinic result = clinicService.createClinic(clinic);

        assertNotNull(result);
        assertEquals(savedClinic, result);
        verify(clinicRepository).save(clinic);
    }

    @Test
    void createClinicThrowsExceptionWhenClinicIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicService.createClinic(null));

        assertEquals("Clinic cannot be null", exception.getMessage());
        verify(clinicRepository, never()).save(any());
    }

    @Test
    void updateClinicSuccessfullyUpdatesExistingClinic() {
        Long id = 1L;
        Clinic existingClinic = createClinic(id, "Old Name", "Old Address", "123456", "Old Specialty", 50);
        Clinic updatedClinic = createClinic(id, "New Name", "New Address", "654321", "New Specialty", 75);

        when(clinicRepository.existsById(id)).thenReturn(true);
        when(clinicRepository.findById(id)).thenReturn(Optional.of(existingClinic));
        when(clinicRepository.save(existingClinic)).thenReturn(existingClinic);

        Clinic result = clinicService.updateClinic(id, updatedClinic);

        assertNotNull(result);
        verify(clinicRepository).save(existingClinic);

        // Verify that the existing clinic was updated with the new values
        assertEquals("New Name", existingClinic.getName());
        assertEquals("New Address", existingClinic.getAddress());
        assertEquals("654321", existingClinic.getPhone());
        assertEquals("New Specialty", existingClinic.getSpecialty());
        assertEquals(75, existingClinic.getCapacity());
    }

    @Test
    void updateClinicThrowsExceptionWhenClinicIsNull() {
        Long id = 1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicService.updateClinic(id, null));

        assertEquals("Clinic cannot be null", exception.getMessage());
        verify(clinicRepository, never()).save(any());
    }

    @Test
    void updateClinicThrowsExceptionWhenClinicDoesNotExist() {
        Long id = 1L;
        Clinic clinic = createClinic(id, "Test Clinic", "Test Address", "123456", "Oncology", 100);

        when(clinicRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicService.updateClinic(id, clinic));

        assertEquals("Clinic with id " + id + " does not exist", exception.getMessage());
        verify(clinicRepository, never()).save(any());
    }

    @Test
    void deleteClinicSuccessfullyDeletesClinic() {
        Long id = 1L;

        when(clinicRepository.existsById(id)).thenReturn(true);

        clinicService.deleteClinic(id);

        verify(clinicRepository).deleteById(id);
    }

    @Test
    void deleteClinicThrowsExceptionWhenClinicDoesNotExist() {
        Long id = 1L;

        when(clinicRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clinicService.deleteClinic(id));

        assertEquals("Clinic with id " + id + " does not exist", exception.getMessage());
        verify(clinicRepository, never()).deleteById(any());
    }

    private Clinic createClinic(Long id, String name, String address, String phone, String specialty, int capacity) {
        Clinic clinic = new Clinic();
        clinic.setId(id);
        clinic.setName(name);
        clinic.setAddress(address);
        clinic.setPhone(phone);
        clinic.setSpecialty(specialty);
        clinic.setCapacity(capacity);
        return clinic;
    }
}