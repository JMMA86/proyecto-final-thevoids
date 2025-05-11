package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.entity.UserSpecialtyDTO;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.UserSpecialtyMapper;
import org.thevoids.oncologic.service.UserSpecialtyService;

@RestController
@RequestMapping("/api/v1/user-specialties")
public class RestUserSpecialtyController {

    @Autowired
    private UserSpecialtyService userSpecialtyService;

    @Autowired
    private UserSpecialtyMapper userSpecialtyMapper;

    @PreAuthorize("hasAuthority('VIEW_USER_SPECIALTIES')")
    @GetMapping
    public ResponseEntity<List<UserSpecialtyDTO>> getAllUserSpecialties() {
        try {
            List<UserSpecialty> userSpecialties = userSpecialtyService.getAllUserSpecialties();
            List<UserSpecialtyDTO> userSpecialtyDTOs = userSpecialties.stream()
                    .map(userSpecialtyMapper::toUserSpecialtyDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userSpecialtyDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('VIEW_USER_SPECIALTIES')")
    @GetMapping("/{id}")
    public ResponseEntity<UserSpecialtyDTO> getUserSpecialtyById(@PathVariable Long id) {
        try {
            UserSpecialty userSpecialty = userSpecialtyService.getUserSpecialtyById(id);
            UserSpecialtyDTO userSpecialtyDTO = userSpecialtyMapper.toUserSpecialtyDTO(userSpecialty);
            return ResponseEntity.ok(userSpecialtyDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_SPECIALTIES')")
    @PostMapping("/users/{userId}/specialties/{specialtyId}")
    public ResponseEntity<UserSpecialtyDTO> assignSpecialtyToUser(
            @PathVariable Long userId,
            @PathVariable Long specialtyId) {
        try {
            userSpecialtyService.addSpecialtyToUser(userId, specialtyId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_SPECIALTIES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSpecialty(@PathVariable Long id) {
        try {
            userSpecialtyService.deleteUserSpecialty(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 