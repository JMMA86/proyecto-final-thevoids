package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.ClinicAssignmentDTO;
import org.thevoids.oncologic.entity.ClinicAssignment;
import org.thevoids.oncologic.mapper.ClinicAssignmentMapper;
import org.thevoids.oncologic.service.ClinicAssigmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clinic-assignments")
public class RestClinicAssignmentController {

    @Autowired
    private ClinicAssigmentService clinicAssigmentService;
    @Autowired
    private ClinicAssignmentMapper clinicAssignmentMapper;

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public List<ClinicAssignmentDTO> getAllClinicAssignments() {
        return clinicAssigmentService.getAllClinicAssignments().stream()
                .map(clinicAssignmentMapper::toClinicAssignmentDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicAssignmentDTO> getClinicAssignmentById(@PathVariable Long id) {
        ClinicAssignment assignment = clinicAssigmentService.getClinicAssignmentById(id);
        if (assignment == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clinicAssignmentMapper.toClinicAssignmentDTO(assignment));
    }

    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<ClinicAssignmentDTO> createClinicAssignment(@RequestBody ClinicAssignmentDTO dto) {
        try {
            // You may need to implement a createClinicAssignment method in the service
            // For now, let's assume updateClinicAssignment can be used for both create and
            // update
            ClinicAssignment assignment = clinicAssignmentMapper.toClinicAssignment(dto);
            ClinicAssignment created = clinicAssigmentService.updateClinicAssignment(assignment);
            return ResponseEntity.ok(clinicAssignmentMapper.toClinicAssignmentDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicAssignmentDTO> updateClinicAssignment(@PathVariable Long id,
            @RequestBody ClinicAssignmentDTO dto) {
        try {
            ClinicAssignment assignment = clinicAssignmentMapper.toClinicAssignment(dto);
            assignment.setId(id);
            ClinicAssignment updated = clinicAssigmentService.updateClinicAssignment(assignment);
            return ResponseEntity.ok(clinicAssignmentMapper.toClinicAssignmentDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicAssignment(@PathVariable Long id) {
        try {
            clinicAssigmentService.deleteClinicAssigment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
