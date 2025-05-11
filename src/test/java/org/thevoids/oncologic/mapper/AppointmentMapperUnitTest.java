package org.thevoids.oncologic.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.thevoids.oncologic.dto.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.entity.Patient;
import org.thevoids.oncologic.entity.User;

class AppointmentMapperUnitTest {

    private final AppointmentMapper mapper = AppointmentMapper.INSTANCE;

    @Test
    void toAppointmentDTO_and_toAppointment() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDateTime(new Date());
        appointment.setStatus("pending");

        Patient patient = new Patient();
        patient.setPatientId(2L);
        appointment.setPatient(patient);

        User doctor = new User();
        doctor.setUserId(3L);
        appointment.setDoctor(doctor);

        AppointmentType type = new AppointmentType();
        type.setTypeId(4L);
        appointment.setAppointmentType(type);

        ClinicAssignment assignment = new ClinicAssignment();
        assignment.setId(5L);
        appointment.setClinicAssignment(assignment);

        AppointmentDTO dto = mapper.toAppointmentDTO(appointment);

        assertEquals(1L, dto.getAppointmentId());
        assertEquals(2L, dto.getPatientId());
        assertEquals(3L, dto.getDoctorId());
        assertEquals(4L, dto.getAppointmentTypeId());
        assertEquals(5L, dto.getClinicAssignmentId());
        assertEquals("pending", dto.getStatus());

        // Reverse mapping
        Appointment mappedBack = mapper.toAppointment(dto);
        assertEquals(dto.getAppointmentId(), mappedBack.getAppointmentId());
        assertEquals(dto.getPatientId(), mappedBack.getPatient().getPatientId());
        assertEquals(dto.getDoctorId(), mappedBack.getDoctor().getUserId());
        assertEquals(dto.getAppointmentTypeId(), mappedBack.getAppointmentType().getTypeId());
        assertEquals(dto.getClinicAssignmentId(), mappedBack.getClinicAssignment().getId());
        assertEquals(dto.getStatus(), mappedBack.getStatus());
    }
}
