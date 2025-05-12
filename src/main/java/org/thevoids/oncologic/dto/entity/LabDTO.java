package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabDTO {
    private Long labId;
    private Long patientId;
    private Long labTechnicianId;
    private String testType;
    private Date requestDate;
    private Date completionDate;
    private String result;
    private String attachment;
}