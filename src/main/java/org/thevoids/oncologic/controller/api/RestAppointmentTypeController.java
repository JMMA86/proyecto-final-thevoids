package org.thevoids.oncologic.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.mapper.AppointmentTypeMapper;
import org.thevoids.oncologic.service.AppointmentTypeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointment-types")
public class RestAppointmentTypeController {

    @Autowired
    private AppointmentTypeService appointmentTypeService;
    @Autowired
    private AppointmentTypeMapper appointmentTypeMapper;

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping
    public List<AppointmentTypeDTO> getAllAppointmentTypes() {
        return appointmentTypeService.getAllAppointmentTypes().stream()
                .map(appointmentTypeMapper::toAppointmentTypeDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('VIEW_APPOINTMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> getAppointmentTypeById(@PathVariable Long id) {
        AppointmentType type = appointmentTypeService.getAppointmentTypeById(id);
        if (type == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(type));
    }

    @PreAuthorize("hasAuthority('ADD_APPOINTMENTS')")
    @PostMapping
    public ResponseEntity<AppointmentTypeDTO> createAppointmentType(@RequestBody AppointmentTypeDTO dto) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            AppointmentType created = appointmentTypeService.createAppointmentType(type);
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('EDIT_APPOINTMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentTypeDTO> updateAppointmentType(@PathVariable Long id,
            @RequestBody AppointmentTypeDTO dto) {
        try {
            AppointmentType type = appointmentTypeMapper.toAppointmentType(dto);
            type.setTypeId(id);
            AppointmentType updated = appointmentTypeService.updateAppointmentType(type);
            return ResponseEntity.ok(appointmentTypeMapper.toAppointmentTypeDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('DELETE_APPOINTMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentType(@PathVariable Long id) {
        try {
            appointmentTypeService.deleteAppointmentType(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
