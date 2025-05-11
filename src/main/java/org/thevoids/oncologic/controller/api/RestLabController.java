package org.thevoids.oncologic.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.service.LabService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/labs")
public class RestLabController {
    private final LabService labService;

    public RestLabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public ResponseEntity<List<LabDTO>> getAllLabs() {
        try {
            List<LabDTO> labs = labService.getAllLabs();
            return ResponseEntity.ok(labs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLabById(@PathVariable Long id) {
        try {
            LabDTO lab = labService.getLabById(id);
            return ResponseEntity.ok(lab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignLab(
            @RequestParam Long patientId,
            @RequestParam Long technicianId,
            @RequestParam Date requestDate) {
        try {
            LabDTO createdLab = labService.assignLab(patientId, technicianId, requestDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLab(@PathVariable Long id, @RequestBody LabDTO labDTO) {
        try {
            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLab(labDTO);
            return ResponseEntity.ok(updatedLab);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable Long id) {
        try {
            labService.deleteLab(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}