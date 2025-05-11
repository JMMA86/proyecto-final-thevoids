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
import org.thevoids.oncologic.dto.ClinicAssignmentDTO;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.mapper.ClinicAssignmentMapper;
import org.thevoids.oncologic.service.ClinicAssigmentService;

/**
 * REST controller for managing clinic assignments.
 */
@RestController
@RequestMapping("/api/v1/clinic-assignments")
public class RestClinicAssignmentController {

    @Autowired
    private ClinicAssigmentService clinicAssigmentService;
    @Autowired
    private ClinicAssignmentMapper clinicAssignmentMapper;

    /**
     * Retrieves all clinic assignments.
     *
     * @return a list of all clinic assignments as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<ClinicAssignmentDTO>> getAllClinicAssignments() {
        try {
            List<ClinicAssignmentDTO> assignments = clinicAssigmentService.getAllClinicAssignments().stream()
                    .map(clinicAssignmentMapper::toClinicAssignmentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific clinic assignment by its ID.
     *
     * @param id the ID of the clinic assignment to retrieve.
     * @return the clinic assignment with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicAssignmentDTO> getClinicAssignmentById(@PathVariable Long id) {
        try {
            ClinicAssignment assignment = clinicAssigmentService.getClinicAssignmentById(id);
            if (assignment == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(clinicAssignmentMapper.toClinicAssignmentDTO(assignment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new clinic assignment.
     *
     * @param dto the clinic assignment to create as a DTO.
     * @return the created clinic assignment as a DTO.
     */
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<ClinicAssignmentDTO> createClinicAssignment(@RequestBody ClinicAssignmentDTO dto) {
        try {
            ClinicAssignment assignment = clinicAssignmentMapper.toClinicAssignment(dto);
            ClinicAssignment created = clinicAssigmentService.updateClinicAssignment(assignment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clinicAssignmentMapper.toClinicAssignmentDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing clinic assignment.
     *
     * @param id  the ID of the clinic assignment to update.
     * @param dto the updated clinic assignment data.
     * @return the updated clinic assignment as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicAssignmentDTO> updateClinicAssignment(@PathVariable Long id,
            @RequestBody ClinicAssignmentDTO dto) {
        try {
            ClinicAssignment assignment = clinicAssignmentMapper.toClinicAssignment(dto);
            assignment.setId(id);
            ClinicAssignment updated = clinicAssigmentService.updateClinicAssignment(assignment);
            return ResponseEntity.ok(clinicAssignmentMapper.toClinicAssignmentDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a clinic assignment.
     *
     * @param id the ID of the clinic assignment to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicAssignment(@PathVariable Long id) {
        try {
            clinicAssigmentService.deleteClinicAssigment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
