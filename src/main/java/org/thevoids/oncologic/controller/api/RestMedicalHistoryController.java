package org.thevoids.oncologic.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.MedicalHistoryDTO;
import org.thevoids.oncologic.service.MedicalHistoryService;

import java.util.List;
import java.util.Map;

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
            List<MedicalHistoryDTO> medicalHistories = medicalHistoryService.getAllMedicalHistories();
            return ResponseEntity.ok(medicalHistories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalHistoryById(@PathVariable Long id) {
        try {
            MedicalHistoryDTO medicalHistory = medicalHistoryService.getMedicalHistoryById(id);
            return ResponseEntity.ok(medicalHistory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createMedicalHistory(@RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        try {
            MedicalHistoryDTO createdMedicalHistory = medicalHistoryService.createMedicalHistory(medicalHistoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMedicalHistory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalHistory(@PathVariable Long id, @RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        try {
            medicalHistoryDTO.setHistoryId(id);
            MedicalHistoryDTO updatedMedicalHistory = medicalHistoryService.updateMedicalHistory(medicalHistoryDTO);
            return ResponseEntity.ok(updatedMedicalHistory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalHistory(@PathVariable Long id) {
        try {
            medicalHistoryService.deleteMedicalHistory(id);
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