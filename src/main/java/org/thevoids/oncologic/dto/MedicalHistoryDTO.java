package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryDTO {
    private Long historyId;
    private Long patientId;
    private String diagnosis;
    private String treatment;
    private String medications;
    private Timestamp recordDate;
}