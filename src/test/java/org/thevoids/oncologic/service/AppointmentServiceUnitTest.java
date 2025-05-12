package org.thevoids.oncologic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thevoids.oncologic.entity.*;
import org.thevoids.oncologic.repository.AppointmentRepository;
import org.thevoids.oncologic.repository.AppointmentTypeRepository;
import org.thevoids.oncologic.repository.ClinicAssignmentRepository;
import org.thevoids.oncologic.repository.PatientRepository;
import org.thevoids.oncologic.service.impl.AppointmentServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceUnitTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ClinicAssignmentRepository clinicAssignmentRepository;

    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void createAppointmentSuccessfullyCreatesAppointment() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        Patient patient = new Patient();
        ClinicAssignment clinicAssignment = new ClinicAssignment();
        AppointmentType appointmentType = new AppointmentType();
        clinicAssignment.setUser(new User());

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(clinicAssignmentRepository.findById(clinicAssignmentId)).thenReturn(Optional.of(clinicAssignment));
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Capture the saved appointment directly
        Appointment result = appointmentService.createAppointment(patientId, clinicAssignmentId, appointmentTypeId,
                dateTime);

        assertNotNull(result);
        assertEquals(patient, result.getPatient());
        assertEquals(clinicAssignment, result.getClinicAssignment());
        assertEquals(appointmentType, result.getAppointmentType());
        assertEquals(dateTime, result.getDateTime());
        assertEquals(clinicAssignment.getUser(), result.getDoctor());
    }

    @Test
    void createAppointmentThrowsExceptionWhenPatientIdIsNull() {
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(null, clinicAssignmentId, appointmentTypeId, dateTime));

        assertEquals("Missing required parameter(s): patientId", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenClinicAssignmentDoesNotExist() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(new Patient()));
        when(clinicAssignmentRepository.findById(clinicAssignmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, clinicAssignmentId, appointmentTypeId, dateTime));

        assertEquals("ClinicAssignment with id 2 does not exist", exception.getMessage());
    }

    @Test
    void updateAppointmentThrowsExceptionWhenAppointmentIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateAppointment(null));

        assertEquals("Appointment cannot be null", exception.getMessage());
    }

    @Test
    void getAllAppointmentsReturnsAllAppointments() {
        List<Appointment> expectedAppointments = List.of(
                createAppointment(1L),
                createAppointment(2L));

        when(appointmentRepository.findAll()).thenReturn(expectedAppointments);

        List<Appointment> result = appointmentService.getAllAppointments();

        assertEquals(2, result.size());
        assertEquals(expectedAppointments, result);
    }

    @Test
    void getAppointmentByIdReturnsAppointmentWhenExists() {
        Long id = 1L;
        Appointment expectedAppointment = createAppointment(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(expectedAppointment));

        Appointment result = appointmentService.getAppointmentById(id);

        assertNotNull(result);
        assertEquals(id, result.getAppointmentId());
    }

    @Test
    void getAppointmentByIdThrowsExceptionWhenNotExists() {
        Long id = 1L;

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.getAppointmentById(id));

        assertEquals("Appointment with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenClinicAssignmentIdIsNull() {
        Long patientId = 1L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, null, appointmentTypeId, dateTime));

        assertEquals("Missing required parameter(s): clinicAssignmentId", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenAppointmentTypeIdIsNull() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Date dateTime = new Date();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, clinicAssignmentId, null, dateTime));

        assertEquals("Missing required parameter(s): appointmentTypeId", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenDateTimeIsNull() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, clinicAssignmentId, appointmentTypeId, null));

        assertEquals("Missing required parameter(s): dateTime", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenMultipleParametersAreNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(null, null, null, null));

        assertEquals("Missing required parameter(s): patientId, clinicAssignmentId, appointmentTypeId, dateTime",
                exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenPatientDoesNotExist() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, clinicAssignmentId, appointmentTypeId, dateTime));

        assertEquals("Patient with id 1 does not exist", exception.getMessage());
    }

    @Test
    void createAppointmentThrowsExceptionWhenAppointmentTypeDoesNotExist() {
        Long patientId = 1L;
        Long clinicAssignmentId = 2L;
        Long appointmentTypeId = 3L;
        Date dateTime = new Date();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(new Patient()));
        when(clinicAssignmentRepository.findById(clinicAssignmentId)).thenReturn(Optional.of(new ClinicAssignment()));
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.createAppointment(patientId, clinicAssignmentId, appointmentTypeId, dateTime));

        assertEquals("AppointmentType with id 3 does not exist", exception.getMessage());
    }

    @Test
    void updateAppointmentSuccessfullyUpdatesAppointment() {
        Long id = 1L;
        Appointment appointment = createAppointment(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.updateAppointment(appointment);

        assertNotNull(result);
        assertEquals(appointment, result);
    }

    @Test
    void updateAppointmentThrowsExceptionWhenIdIsNull() {
        Appointment appointment = createAppointment(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateAppointment(appointment));

        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }

    @Test
    void updateAppointmentThrowsExceptionWhenAppointmentDoesNotExist() {
        Long id = 1L;
        Appointment appointment = createAppointment(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.updateAppointment(appointment));

        assertEquals("Appointment with id 1 does not exist", exception.getMessage());
    }

    @Test
    void deleteAppointmentSuccessfullyDeletesAppointment() {
        Long id = 1L;

        when(appointmentRepository.existsById(id)).thenReturn(true);

        appointmentService.deleteAppointment(id);

        verify(appointmentRepository).deleteById(id);
    }

    @Test
    void deleteAppointmentThrowsExceptionWhenAppointmentDoesNotExist() {
        Long id = 1L;

        when(appointmentRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> appointmentService.deleteAppointment(id));

        assertEquals("Appointment with id 1 does not exist", exception.getMessage());
    }

    private Appointment createAppointment(Long id) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(id);
        return appointment;
    }
}
