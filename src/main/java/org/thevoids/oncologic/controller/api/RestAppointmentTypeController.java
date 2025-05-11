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
import org.thevoids.oncologic.dto.entity.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.mapper.AppointmentTypeMapper;
import org.thevoids.oncologic.service.AppointmentTypeService;

/**
 * REST controller for managing appointment types.
 */
@RestController
@RequestMapping("/api/v1/appointment-types")
public class RestAppointmentTypeController {

    @Autowired
    private AppointmentTypeService appointmentTypeService;
    @Autowired
    private AppointmentTypeMapper appointmentTypeMapper;

    /**
     * Retrieves all appointment types.
     *
     * @return a list of all appointment types as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<AppointmentTypeDTO>> getAllAppointmentTypes() {
        try {
            List<AppointmentTypeDTO> types = appointmentTypeService.getAllAppointmentTypes().stream()
                    .map(appointmentTypeMapper::toAppointmentTypeDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific appointment type by its ID.
     *
     * @param id the ID of the appointment type to retrieve.
     * @return the appointment type with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> getAppointmentTypeById(@PathVariable Long id) {
        try {
            AppointmentType type = appointmentTypeService.getAppointmentTypeById(id);
            if (type == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(type));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new appointment type.
     *
     * @param dto the appointment type to create as a DTO.
     * @return the created appointment type as a DTO.
     */
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentTypeDTO> createAppointmentType(@RequestBody AppointmentTypeDTO dto) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            AppointmentType created = appointmentTypeService.createAppointmentType(type);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentTypeMapper.toAppointmentTypeDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing appointment type.
     *
     * @param id  the ID of the appointment type to update.
     * @param dto the updated appointment type data.
     * @return the updated appointment type as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> updateAppointmentType(@PathVariable Long id,
            @RequestBody AppointmentTypeDTO dto) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            type.setTypeId(id);
            AppointmentType updated = appointmentTypeService.updateAppointmentType(type);
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an appointment type.
     *
     * @param id the ID of the appointment type to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentType(@PathVariable Long id) {
        try {
            appointmentTypeService.deleteAppointmentType(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
