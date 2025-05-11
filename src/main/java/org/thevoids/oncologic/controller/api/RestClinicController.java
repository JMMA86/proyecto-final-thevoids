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
import org.thevoids.oncologic.dto.entity.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.mapper.ClinicMapper;
import org.thevoids.oncologic.service.ClinicService;

/**
 * REST controller for managing clinics.
 */
@RestController
@RequestMapping("/api/v1/clinics")
public class RestClinicController {

    @Autowired
    private ClinicService clinicService;
    @Autowired
    private ClinicMapper clinicMapper;

    /**
     * Retrieves all clinics.
     *
     * @return a list of all clinics as DTOs.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public ResponseEntity<List<ClinicDTO>> getAllClinics() {
        try {
            List<ClinicDTO> clinics = clinicService.getAllClinics().stream()
                    .map(clinicMapper::toClinicDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(clinics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific clinic by its ID.
     *
     * @param id the ID of the clinic to retrieve.
     * @return the clinic with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicDTO> getClinicById(@PathVariable Long id) {
        try {
            Clinic clinic = clinicService.getClinicById(id);
            if (clinic == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.ok(clinicMapper.toClinicDTO(clinic));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new clinic.
     *
     * @param dto the clinic to create as a DTO.
     * @return the created clinic as a DTO.
     */
    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<ClinicDTO> createClinic(@RequestBody ClinicDTO dto) {
        try {
            Clinic clinic = clinicMapper.toClinic(dto);
            Clinic created = clinicService.createClinic(clinic);
            return ResponseEntity.status(HttpStatus.CREATED).body(clinicMapper.toClinicDTO(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing clinic.
     *
     * @param id  the ID of the clinic to update.
     * @param dto the updated clinic data.
     * @return the updated clinic as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicDTO> updateClinic(@PathVariable Long id, @RequestBody ClinicDTO dto) {
        try {
            Clinic existing = clinicService.getClinicById(id);
            if (existing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            // Only update fields from DTO
            if (dto.getName() != null)
                existing.setName(dto.getName());
            if (dto.getAddress() != null)
                existing.setAddress(dto.getAddress());
            if (dto.getPhone() != null)
                existing.setPhone(dto.getPhone());
            if (dto.getSpecialty() != null)
                existing.setSpecialty(dto.getSpecialty());
            if (dto.getCapacity() != null)
                existing.setCapacity(dto.getCapacity());

            Clinic updated = clinicService.updateClinic(id, existing);
            return ResponseEntity.ok(clinicMapper.toClinicDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a clinic.
     *
     * @param id the ID of the clinic to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        try {
            clinicService.deleteClinic(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
