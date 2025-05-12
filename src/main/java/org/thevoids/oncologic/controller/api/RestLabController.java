package org.thevoids.oncologic.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.LabDTO;
import org.thevoids.oncologic.service.LabService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

import java.util.Date;
import java.util.List;

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabDTO> getLabById(@PathVariable Long id) {
        try {
            LabDTO lab = labService.getLabById(id);
            return ResponseEntity.ok(lab);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping
    public ResponseEntity<LabDTO> assignLab(@RequestBody LabDTO labDTO) {
        try {
            Long patientId = labDTO.getPatientId();
            Long userId = labDTO.getLabTechnicianId();
            Date requestDate = labDTO.getRequestDate();
            LabDTO createdLabDTO = labService.assignLab(patientId, userId, requestDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLabDTO);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabDTO> updateLab(@PathVariable Long id, @RequestBody LabDTO labDTO) {
        try {
            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLab(labDTO);
            return ResponseEntity.ok(updatedLab);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
        try {
            labService.deleteLab(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
