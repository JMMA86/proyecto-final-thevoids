package org.thevoids.oncologic.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.MedicalHistoryDTO;
import org.thevoids.oncologic.service.MedicalHistoryService;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.exception.InvalidOperationException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-histories")
public class RestMedicalHistoryController {
    private final MedicalHistoryService medicalHistoryService;

    public RestMedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryDTO>> getAllMedicalHistories() {
        try {
            List<MedicalHistoryDTO> histories = medicalHistoryService.getAllMedicalHistories();
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistoryById(@PathVariable Long id) {
        try {
            MedicalHistoryDTO history = medicalHistoryService.getMedicalHistoryById(id);
            return ResponseEntity.ok(history);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> createMedicalHistory(@RequestBody MedicalHistoryDTO dto) {
        try {
            MedicalHistoryDTO created = medicalHistoryService.createMedicalHistory(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDTO> updateMedicalHistory(@PathVariable Long id,
            @RequestBody MedicalHistoryDTO dto) {
        try {
            dto.setHistoryId(id);
            MedicalHistoryDTO updated = medicalHistoryService.updateMedicalHistory(dto);
            return ResponseEntity.ok(updated);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable Long id) {
        try {
            medicalHistoryService.deleteMedicalHistory(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
