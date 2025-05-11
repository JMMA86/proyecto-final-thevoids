package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.mapper.ClinicMapper;
import org.thevoids.oncologic.service.ClinicService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clinics")
public class RestClinicController {

    @Autowired
    private ClinicService clinicService;
    @Autowired
    private ClinicMapper clinicMapper;

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public List<ClinicDTO> getAllClinics() {
        return clinicService.getAllClinics().stream()
                .map(clinicMapper::toClinicDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicDTO> getClinicById(@PathVariable Long id) {
        Clinic clinic = clinicService.getClinicById(id);
        if (clinic == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clinicMapper.toClinicDTO(clinic));
    }

    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<ClinicDTO> createClinic(@RequestBody ClinicDTO dto) {
        try {
            Clinic clinic = clinicMapper.toClinic(dto);
            Clinic created = clinicService.createClinic(clinic);
            return ResponseEntity.ok(clinicMapper.toClinicDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicDTO> updateClinic(@PathVariable Long id, @RequestBody ClinicDTO dto) {
        try {
            Clinic clinic = clinicMapper.toClinic(dto);
            Clinic updated = clinicService.updateClinic(id, clinic);
            return ResponseEntity.ok(clinicMapper.toClinicDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        try {
            clinicService.deleteClinic(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
