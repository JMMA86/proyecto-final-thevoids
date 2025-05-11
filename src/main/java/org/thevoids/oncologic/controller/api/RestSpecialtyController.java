package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.entity.SpecialtyDTO;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.SpecialtyMapper;
import org.thevoids.oncologic.service.SpecialtyService;

@RestController
@RequestMapping("/api/v1/specialties")
public class RestSpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @PreAuthorize("hasAuthority('VIEW_SPECIALTIES')")
    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        try {
            List<Specialty> specialties = specialtyService.getAllSpecialties();
            List<SpecialtyDTO> specialtyDTOs = specialties.stream()
                    .map(specialtyMapper::toSpecialtyDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(specialtyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('VIEW_SPECIALTIES')")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(@PathVariable Long id) {
        try {
            Specialty specialty = specialtyService.getSpecialtyById(id);
            SpecialtyDTO specialtyDTO = specialtyMapper.toSpecialtyDTO(specialty);
            return ResponseEntity.ok(specialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('ADD_SPECIALTIES')")
    @PostMapping
    public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        try {
            Specialty specialty = specialtyMapper.toSpecialty(specialtyDTO);
            Specialty createdSpecialty = specialtyService.createSpecialty(specialty);
            SpecialtyDTO createdSpecialtyDTO = specialtyMapper.toSpecialtyDTO(createdSpecialty);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpecialtyDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_SPECIALTIES')")
    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(@PathVariable Long id, @RequestBody SpecialtyDTO specialtyDTO) {
        try {
            Specialty specialty = specialtyMapper.toSpecialty(specialtyDTO);
            specialty.setSpecialtyId(id);
            Specialty updatedSpecialty = specialtyService.updateSpecialty(specialty);
            SpecialtyDTO updatedSpecialtyDTO = specialtyMapper.toSpecialtyDTO(updatedSpecialty);
            return ResponseEntity.ok(updatedSpecialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_SPECIALTIES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        try {
            specialtyService.deleteSpecialty(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 