package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.AppointmentDTO;
import org.thevoids.oncologic.entity.Appointment;
import org.thevoids.oncologic.mapper.AppointmentMapper;
import org.thevoids.oncologic.service.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointments")
public class RestAppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentMapper appointmentMapper;

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentService.getAllAppointments().stream()
                .map(appointmentMapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(appointment));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        try {
            Appointment appointment = appointmentService.createAppointment(
                    dto.getPatientId(),
                    dto.getClinicAssignmentId(),
                    dto.getAppointmentTypeId(),
                    dto.getDateTime());
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO dto) {
        try {
            Appointment appointment = appointmentMapper.toAppointment(dto);
            appointment.setAppointmentId(id);
            Appointment updated = appointmentService.updateAppointment(appointment);
            return ResponseEntity.ok(appointmentMapper.toAppointmentDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
