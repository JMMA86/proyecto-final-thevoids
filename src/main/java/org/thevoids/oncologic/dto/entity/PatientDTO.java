package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long patientId;
    private Long userId;
    private String bloodGroup;
    private String allergies;
    private String familyHistory;
}