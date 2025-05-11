package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.entity.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.mapper.AppointmentMapper;
import org.thevoids.oncologic.service.AppointmentService;

/**
 * REST controller for managing appointments.
 */
@RestController
@RequestMapping("/api/v1/appointments")
public class RestAppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * Retrieves all appointments.
     *
     * @return a list of all appointments as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        try {
            List<AppointmentDTO> appointments = appointmentService.getAllAppointments().stream()
                    .map(appointmentMapper::toAppointmentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific appointment by its ID.
     *
     * @param id the ID of the appointment to retrieve.
     * @return the appointment with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new appointment.
     *
     * @param dto the appointment to create as a DTO.
     * @return the created appointment as a DTO.
     */
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        try {
            Appointment appointment = appointmentService.createAppointment(
                    dto.getPatientId(),
                    dto.getClinicAssignmentId(),
                    dto.getAppointmentTypeId(),
                    dto.getDateTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toAppointmentDTO(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing appointment.
     *
     * @param id  the ID of the appointment to update.
     * @param dto the updated appointment data.
     * @return the updated appointment as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO dto) {
        try {
            Appointment existing = appointmentService.getAppointmentById(id);
            // Only update fields from DTO
            if (dto.getDateTime() != null)
                existing.setDateTime(dto.getDateTime());
            if (dto.getStatus() != null)
                existing.setStatus(dto.getStatus());
            // Optionally update related entities if IDs are provided
            if (dto.getPatientId() != null)
                existing.getPatient().setPatientId(dto.getPatientId());
            if (dto.getDoctorId() != null)
                existing.getDoctor().setUserId(dto.getDoctorId());
            if (dto.getAppointmentTypeId() != null)
                existing.getAppointmentType().setTypeId(dto.getAppointmentTypeId());
            if (dto.getClinicAssignmentId() != null)
                existing.getClinicAssignment().setId(dto.getClinicAssignmentId());

            Appointment updated = appointmentService.updateAppointment(existing);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an appointment.
     *
     * @param id the ID of the appointment to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
