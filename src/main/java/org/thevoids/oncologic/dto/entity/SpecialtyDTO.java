package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialtyDTO {
    private Long specialtyId;
    private String specialtyName;

    public SpecialtyDTO(Long specialtyId, String specialtyName) {
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
    }

    public SpecialtyDTO() {
    }
} 