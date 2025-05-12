package org.thevoids.oncologic.mapper;

import org.springframework.stereotype.Component;
import org.thevoids.oncologic.dto.entity.SpecialtyDTO;
import org.thevoids.oncologic.entity.Specialty;

@Component
public class SpecialtyMapper {
    
    public SpecialtyDTO toSpecialtyDTO(Specialty specialty) {
        if (specialty == null) {
            return null;
        }
        
        return new SpecialtyDTO(
            specialty.getSpecialtyId(),
            specialty.getSpecialtyName()
        );
    }
    
    public Specialty toSpecialty(SpecialtyDTO specialtyDTO) {
        if (specialtyDTO == null) {
            return null;
        }
        
        Specialty specialty = new Specialty();
        specialty.setSpecialtyId(specialtyDTO.getSpecialtyId());
        specialty.setSpecialtyName(specialtyDTO.getSpecialtyName());
        return specialty;
    }
} 